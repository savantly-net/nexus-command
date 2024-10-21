package net.savantly.nexus.flow.dom.connections.webhook;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.apache.causeway.applib.services.repository.RepositoryService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destination.AbstractBaseDestinationHook;
import net.savantly.nexus.flow.dom.destination.Destination;
import net.savantly.nexus.flow.dom.destination.DestinationHookResponse;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.dom.formMapping.Mapping;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.webhooks.dom.webhook.Webhooks;

@Log4j2
public class WebhookDestinationHook extends AbstractBaseDestinationHook {

    final private Webhooks webhooks;
    final RestTemplateBuilder restTemplateBuilder;

    public WebhookDestinationHook(FlowContextFactory flowContextFactory, JavascriptExecutor javascriptExecutor,
            RepositoryService repositoryService, Webhooks webhooks, RestTemplateBuilder restTemplateBuilder,
            ObjectMapper objectMapper) {
        super(flowContextFactory, javascriptExecutor, repositoryService, objectMapper);
        this.webhooks = webhooks;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public DestinationHookResponse sendData(Destination destination, Map<String, Object> payload,
            Collection<? extends Mapping> formMappings) {
        var webhook = webhooks.findById(destination.getDestinationId()).orElseThrow();

        var headers = new HttpHeaders();
        if (Objects.nonNull(webhook.getHeaders())) {
            var headerLines = webhook.getHeaders().split("\n");
            for (var headerLine : headerLines) {
                var parts = headerLine.split(":");
                headers.add(parts[0], parts[1]);
            }
        }

        var template = restTemplateBuilder.build();
        var method = HttpMethod.valueOf(webhook.getMethod().name());

        var request = RequestEntity.method(method, webhook.getUrl()).headers(headers).body(payload);

        log.info("Sending webhook request: {}", request);

        try {
            var response = template.exchange(request, String.class);
            return new DestinationHookResponse().setSuccess(response.getStatusCode().is2xxSuccessful())
                    .setMessage(response.getBody());
        } catch (Exception e) {
            return new DestinationHookResponse().setSuccess(false).setMessage(e.getMessage());
        }
    }

}
