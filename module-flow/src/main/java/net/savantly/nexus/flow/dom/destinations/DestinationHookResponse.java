package net.savantly.nexus.flow.dom.destinations;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DestinationHookResponse {
    
    private boolean success;
    private String message;
}
