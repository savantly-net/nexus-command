package net.savantly.nexus.flow.nodes;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowNode.FlowNode;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeType;

@Log4j2
@FlowNodeType(name = "Logging", description = "Log the context")
public class LoggingNode implements FlowNode {
    private final String id;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoggingNode(String id) {
        this.id = id;
    }

    @Override
    public void execute(FlowContext context) {
        log.info("Executing node: " + id);
        try {
            log.info("Context: " + objectMapper.writeValueAsString(context));
        } catch (Exception e) {
            log.error("Error serializing context", e);
        }
    }

    @Override
    public String getId() {
        return id;
    }
}