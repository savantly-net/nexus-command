package net.savantly.nexus.flow.dom.flowNode;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Set;

import org.junit.jupiter.api.Test;

public class FlowNodeDiscoveryServiceTest {
    
    @Test
    public void testDiscoverFlowNodes() {
        FlowNodeDiscoveryService service = new FlowNodeDiscoveryService();
        Set<FlowNodeDescriptor> descriptors = service.discoverFlowNodes();
        assertFalse(descriptors.isEmpty());
    }
}
