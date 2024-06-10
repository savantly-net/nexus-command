package net.savantly.nexus.flow.dom.flowNode;

import net.savantly.nexus.flow.dom.flowContext.FlowContext;

public interface FlowNode {
    void execute(FlowContext context);
    String getId();
}