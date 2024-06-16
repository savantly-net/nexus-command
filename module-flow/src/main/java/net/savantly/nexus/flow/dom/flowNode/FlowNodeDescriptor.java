package net.savantly.nexus.flow.dom.flowNode;

import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameterDescriptor;

@Data
public class FlowNodeDescriptor {
    
    private String className;
    private String name;
    private String description;
    private Set<FlowNodeParameterDescriptor> inputParameters = Set.of();
    private JsonNode schema;
    
}
