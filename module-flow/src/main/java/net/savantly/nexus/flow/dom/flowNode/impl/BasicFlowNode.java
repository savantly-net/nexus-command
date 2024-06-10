package net.savantly.nexus.flow.dom.flowNode.impl;

import lombok.Getter;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNode;
import net.savantly.nexus.flow.dom.flowNode.Parameter;

public abstract class BasicFlowNode implements FlowNode {
    private String id;


    @Parameter("label")
    @Getter
    private String label;

    public BasicFlowNode(String id) {
        this.id = id;
    }

    @Override
    public abstract void execute(FlowContext context);

    @Override
    public String getId() {
        return id;
    }
}
