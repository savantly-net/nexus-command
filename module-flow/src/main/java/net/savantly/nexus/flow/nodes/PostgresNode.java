package net.savantly.nexus.flow.nodes;

import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;
import net.savantly.nexus.flow.dom.flowNode.impl.BasicFlowNode;

@FlowNodeType(name = "postgres", description = "Interact with a Postgres database")
public class PostgresNode extends BasicFlowNode {

    public PostgresNode(String id) {
        super(id);
    }

    @Override
    public void execute(FlowContext context) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }
    
}
