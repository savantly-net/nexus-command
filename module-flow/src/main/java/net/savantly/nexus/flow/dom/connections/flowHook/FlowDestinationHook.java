package net.savantly.nexus.flow.dom.connections.flowHook;

import java.util.Collection;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.destinations.Destination;
import net.savantly.nexus.flow.dom.destinations.DestinationHook;
import net.savantly.nexus.flow.dom.destinations.DestinationHookResponse;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitionExecutionProxy;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.formMapping.Mapping;

@RequiredArgsConstructor
public class FlowDestinationHook implements DestinationHook {

    private final FlowDefinitions flowDefinitions;
    private final FlowDefinitionExecutionProxy flowDefinitionExecutionProxy;

    @Override
    public DestinationHookResponse execute(Destination destination, Map<String, Object> payload,
            Collection<? extends Mapping> formMappings) {

        var flow = flowDefinitions.findById(destination.getDestinationId()).orElseThrow();

        var result = flowDefinitionExecutionProxy.executeFlow(flow, payload);


        return new DestinationHookResponse()
            .setSuccess(true)
            .setMessage("Flow executed successfully: " + result.getId());
    }

    @Override
    public void close() throws Exception {
    }
    
}
