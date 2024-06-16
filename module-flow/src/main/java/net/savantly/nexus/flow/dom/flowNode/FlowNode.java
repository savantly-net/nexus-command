package net.savantly.nexus.flow.dom.flowNode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import net.savantly.nexus.flow.dom.flowContext.FlowContext;

public interface FlowNode {

    @JsonIgnore
    void execute(FlowContext context);

    @JsonIgnore
    String getId();
}