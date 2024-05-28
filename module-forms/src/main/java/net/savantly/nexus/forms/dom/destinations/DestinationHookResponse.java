package net.savantly.nexus.forms.dom.destinations;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class DestinationHookResponse {
    
    private boolean success;
    private String message;
}
