package net.savantly.nexus.flow.nodes;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNode;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;

@Log4j2
@FlowNodeType(name = "Start", description = "The start node of a flow")
public class StartNode implements FlowNode {
    private final String id;

    public StartNode(String id) {
        this.id = id;
    }

    @Override
    public void execute(FlowContext context) {
        log.info("Executing start node: {}", id);
    }

    @Override
    public String getId() {
        return id;
    }
}
