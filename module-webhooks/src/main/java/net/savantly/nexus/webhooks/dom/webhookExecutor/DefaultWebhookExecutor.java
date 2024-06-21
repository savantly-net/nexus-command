package net.savantly.nexus.webhooks.dom.webhookExecutor;

import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.webhooks.api.WebhookExecutionResponse;
import net.savantly.nexus.webhooks.api.WebhookExecutor;
import net.savantly.nexus.webhooks.dom.webhook.Webhook;

@Log4j2
@RequiredArgsConstructor
public class DefaultWebhookExecutor implements WebhookExecutor {

    final RestTemplateBuilder restTemplateBuilder;

    @Override
    public WebhookExecutionResponse execute(Webhook webhook, String payload) {

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
        return new WebhookExecutionResponse().setSuccess(response.getStatusCode().is2xxSuccessful())
                .setMessage(response.getBody());
    }

}
