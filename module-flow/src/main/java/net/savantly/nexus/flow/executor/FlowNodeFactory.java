package net.savantly.nexus.flow.executor;

import java.lang.reflect.Field;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowNode.FlowNode;
import net.savantly.nexus.flow.dom.flowNodeParameter.FlowNodeParameter;
import net.savantly.nexus.flow.dto.FlowNodeDto;
import net.savantly.nexus.flow.executor.exception.IllegalNodeTypeException;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.flow.nodes.HttpNode;
import net.savantly.nexus.flow.nodes.JavascriptNode;
import net.savantly.nexus.flow.nodes.LoggingNode;
import net.savantly.nexus.flow.nodes.PostgresNode;
import net.savantly.nexus.flow.nodes.StartNode;

@Log4j2
@RequiredArgsConstructor
public class FlowNodeFactory {

    final private JavascriptExecutor javascriptExecutor;

    /**
     * We'll replace this with the Object Mapper once the node type names match the class names
     * Then we might also remove the @FlowNodeParameter annotation because the metadata will be provided by Jackson and openapi annotations
     * @param nodeDto
     * @return
     */
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
            case "http":
                node = new HttpNode(nodeDto.getId());
                break;
            case "postgres":
                node = new PostgresNode(nodeDto.getId());
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
            if (field.isAnnotationPresent(FlowNodeParameter.class)) {
                FlowNodeParameter parameter = field.getAnnotation(FlowNodeParameter.class);
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
