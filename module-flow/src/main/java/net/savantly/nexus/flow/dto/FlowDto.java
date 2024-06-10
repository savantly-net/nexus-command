package net.savantly.nexus.flow.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class FlowDto {

    private String id;
    private String name;
    private String description;

    private Set<FlowNodeDto> nodes = new HashSet<>();
    private Set<FlowEdgeDto> edges = new HashSet<>();

    public FlowDto addNode(FlowNodeDto node) {
        nodes.add(node);
        return this;
    }

    public FlowDto addEdge(FlowEdgeDto edge) {
        edges.add(edge);
        return this;
    }
}
