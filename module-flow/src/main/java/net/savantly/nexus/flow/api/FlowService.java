package net.savantly.nexus.flow.api;

import java.util.Set;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDescriptor;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDiscoveryService;

@RequiredArgsConstructor
public class FlowService {


    private final FlowDefinitions flowDefinitions;
    private final FlowNodeDiscoveryService flowNodeDiscoveryService;

    public FlowDefinitions getFlowDefinitions() {
        return flowDefinitions;
    }

    public Set<FlowNodeDescriptor> getAvailableFlowNodeDescriptors() {
        return flowNodeDiscoveryService.discoverFlowNodes();
    }

    public void executeFlow(String flowId, Object payload, String apiKey) {
        flowDefinitions.executeFlow(flowId, payload, apiKey);
    }
    
}
