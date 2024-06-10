package net.savantly.nexus.flow.dom.connections.flowHook;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitionExecutionProxy;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;

@RequiredArgsConstructor
public class FlowDestinationHookFactory {
    
    private final FlowDefinitions flowDefinitions;
    private final FlowDefinitionExecutionProxy flowDefinitionExecutionProxy;

    public FlowDestinationHook createHook() {
        return new FlowDestinationHook(flowDefinitions, flowDefinitionExecutionProxy);
    }
}
