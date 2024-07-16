package net.savantly.nexus.flow.dom.destination;

import java.util.Collection;
import java.util.Map;

import net.savantly.nexus.flow.dom.formMapping.Mapping;

public interface DestinationHook extends AutoCloseable {
    
    public DestinationHookResponse execute(Destination destination, Map<String, Object> payload, Collection<? extends Mapping> formMappings);
}
