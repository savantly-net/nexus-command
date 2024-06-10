package net.savantly.nexus.flow.executor;

import java.lang.reflect.Field;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowNode.FlowNode;
import net.savantly.nexus.flow.dom.flowNode.Parameter;
import net.savantly.nexus.flow.dto.FlowNodeDto;
import net.savantly.nexus.flow.executor.exception.IllegalNodeTypeException;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.flow.nodes.JavascriptNode;
import net.savantly.nexus.flow.nodes.LoggingNode;
import net.savantly.nexus.flow.nodes.StartNode;

@Log4j2
@RequiredArgsConstructor
public class FlowNodeFactory {

    final private JavascriptExecutor javascriptExecutor;

    public FlowNode createNode(FlowNodeDto nodeDto) {
        FlowNode node;
        var nodeType = nodeDto.getType().toLowerCase();
        log.info("Creating node of type: {}", nodeType);
        switch (nodeType) {
            case "start":
                node = new StartNode(nodeDto.getId());
                break;
            case "logging":
                node = new LoggingNode(nodeDto.getId());
                break;
            case "javascript":
                node = new JavascriptNode(nodeDto.getId(), javascriptExecutor);
                break;
            default:
                throw new IllegalNodeTypeException("Unknown node type: " + nodeDto.getType());
        }
        setNodeParameters(node, nodeDto.getData());
        return node;
    }

    private void setNodeParameters(FlowNode node, Map<String, Object> data) {
        Field[] fields = node.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Parameter.class)) {
                Parameter parameter = field.getAnnotation(Parameter.class);
                String parameterName = parameter.value();
                if (data.containsKey(parameterName)) {
                    field.setAccessible(true);
                    try {
                        field.set(node, data.get(parameterName));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(
                                "Failed to set parameter " + parameterName + " on node " + node.getId(), e);
                    }
                }
            }
        }
    }
}
