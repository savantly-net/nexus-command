package net.savantly.nexus.flow.dom.destination;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DestinationHookResponse {
    
    private boolean success;
    private String message;

    public static DestinationHookResponse success() {
        return new DestinationHookResponse().setSuccess(true);
    }

    public static DestinationHookResponse failure(String message) {
        return new DestinationHookResponse().setSuccess(false).setMessage(message);
    }
}
