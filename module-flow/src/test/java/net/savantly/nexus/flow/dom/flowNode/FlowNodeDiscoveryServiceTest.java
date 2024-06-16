package net.savantly.nexus.flow.dom.flowNode;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.junit.jupiter.api.Test;

import net.savantly.nexus.flow.dom.flowNodeSchema.FlowNodeSchemaGenerator;

public class FlowNodeDiscoveryServiceTest {
    
    @Test
    public void testDiscoverFlowNodes() {
        FlowNodeSchemaGenerator flowNodeSchemaGenerator = new FlowNodeSchemaGenerator();
        FlowNodeDiscoveryService service = new FlowNodeDiscoveryService(flowNodeSchemaGenerator);
        Set<FlowNodeDescriptor> descriptors = service.discoverFlowNodes();
        assertFalse(descriptors.isEmpty());
    }
}
