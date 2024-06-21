package net.savantly.nexus.webhooks.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class WebhookExecutionResponse {
    
    private boolean success;
    private String message;
    
}
