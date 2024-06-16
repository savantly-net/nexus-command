package net.savantly.nexus.flow.nodes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;
import net.savantly.nexus.flow.dom.flowNode.impl.BasicFlowNode;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameter;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

@FlowNodeType(name = "Javascript", description = "Execute a javascript script")
public class JavascriptNode extends BasicFlowNode {

    private final JavascriptExecutor executor;

    @Getter @Setter
    @FlowNodeParameter("script")
    @Schema(description = "The javascript script to execute", format = "javascript")
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
