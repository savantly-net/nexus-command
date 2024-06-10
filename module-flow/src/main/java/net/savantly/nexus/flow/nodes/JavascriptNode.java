package net.savantly.nexus.flow.nodes;

import lombok.Getter;
import lombok.Setter;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;
import net.savantly.nexus.flow.dom.flowNode.Parameter;
import net.savantly.nexus.flow.dom.flowNode.impl.BasicFlowNode;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

@FlowNodeType(name = "Javascript", description = "Execute a javascript script")
public class JavascriptNode extends BasicFlowNode {

    private final JavascriptExecutor executor;

    @Parameter("script")
    @Getter @Setter
    private String script;

    public JavascriptNode(String id, JavascriptExecutor executor) {
        super(id);
        this.executor = executor;
    }

    @Override
    public void execute(FlowContext context) {
        try {
            executor.execute(script, context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
