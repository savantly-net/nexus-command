package net.savantly.nexus.flow.dom.connections.webhook;

import org.apache.causeway.applib.services.repository.RepositoryService;
import org.springframework.boot.web.client.RestTemplateBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.webhooks.dom.webhook.Webhooks;

@RequiredArgsConstructor
public class WebhookDestinationHookFactory {

    private final FlowContextFactory flowContextFactory;
    private final JavascriptExecutor javascriptExecutor;
    private final RepositoryService repositoryService;
    private final Webhooks webhooks;
    private final RestTemplateBuilder restTemplateBuilder;
    private final ObjectMapper objectMapper;

    public WebhookDestinationHook createHook() {
        return new WebhookDestinationHook(flowContextFactory, javascriptExecutor, repositoryService, webhooks,
                restTemplateBuilder, objectMapper);
    }
}
