package net.savantly.nexus.flow.dom.connections.webhook;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destinations.Destination;
import net.savantly.nexus.flow.dom.destinations.DestinationHook;
import net.savantly.nexus.flow.dom.destinations.DestinationHookResponse;
import net.savantly.nexus.flow.dom.formMapping.Mapping;

@Log4j2
@RequiredArgsConstructor
public class WebhookDestinationHook implements DestinationHook {

    final private Webhooks webhooks;
    final RestTemplateBuilder restTemplateBuilder;

    @Override
    public DestinationHookResponse execute(Destination destination, Map<String, Object> payload,
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

        var response = template.exchange(request, String.class);
        return new DestinationHookResponse().setSuccess(response.getStatusCode().is2xxSuccessful())
                .setMessage(response.getBody());
    }

    @Override
    public void close() throws Exception {
    }

}
