package net.savantly.nexus.flow.dom.flowNode;

import java.util.Set;

import lombok.Data;

@Data
public class FlowNodeDescriptor {
    
    private String className;
    private String name;
    private String description;
    private Set<ParameterDescriptor> inputParameters = Set.of();
    
}
