package net.savantly.nexus.forms.dom.destinations;

import java.util.Collection;
import java.util.Map;

import net.savantly.nexus.forms.dom.formMapping.Mapping;

public interface DestinationHook {
    
    public DestinationHookResponse execute(Destination destination, Map<String, Object> payload, Collection<? extends Mapping> formMappings);
}
