package net.savantly.nexus.webhooks.api;

import net.savantly.nexus.webhooks.dom.webhook.Webhook;

public interface WebhookExecutor {

    WebhookExecutionResponse execute(Webhook webhook, String payload);
    
}
