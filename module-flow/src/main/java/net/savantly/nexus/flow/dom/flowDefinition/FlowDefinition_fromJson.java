package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowDefinitionEdge.FlowDefinitionEdge;
import net.savantly.nexus.flow.dom.flowDefinitionNode.FlowDefinitionNode;
import net.savantly.nexus.flow.dto.FlowDto;
import net.savantly.nexus.flow.dto.FlowEdgeDto;
import net.savantly.nexus.flow.dto.FlowNodeDto;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(describedAs = "Update from JSON", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class FlowDefinition_fromJson {

    final FlowDefinition object;
    final ObjectMapper objectMapper = new ObjectMapper();

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FlowDefinition_fromJson> {
    }

    @Inject
    RepositoryService repositoryService;
    @Inject
    MessageService  messageService;

    @MemberSupport
    public FlowDefinition act(
            @ParameterLayout(multiLine = 20) final String json) {

        FlowDto flowDto;

        try {
            flowDto = objectMapper.readValue(json, FlowDto.class);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json", e);
            messageService.raiseError("Failed to parse json: " + e.getMessage());
            return object;
        }

        // update the object with the new values
        object.setName(Optional.ofNullable(flowDto.getName()).orElse(object.getName()));

        // remove the existing nodes
        object.getNodes().forEach(node -> {
            repositoryService.remove(node);
        });

        // add the new nodes
        object.setNodes(convertNodes(flowDto.getNodes()));

        // remove edges
        object.getEdges().forEach(edge -> {
            repositoryService.remove(edge);
        });

        // add the new edges
        object.setEdges(convertEdges(flowDto.getEdges()));

        return repositoryService.persist(object);
    }

    private Set<FlowDefinitionNode> convertNodes(Set<FlowNodeDto> nodes) {
        return nodes.stream()
                .map(nodeDto -> convertNode(nodeDto))
                .collect(Collectors.toSet());
    }

    private FlowDefinitionNode convertNode(FlowNodeDto nodeDto) {
        var nodeEntity = FlowDefinitionNode.withId(object, nodeDto.getId());
        nodeEntity.setNodeData(serialize(nodeDto.getData()));
        nodeEntity.setNodePosition(serialize(nodeDto.getPosition()));
        nodeEntity.setNodeStyle(serialize(nodeDto.getStyle()));
        nodeEntity.setNodeType(nodeDto.getType());
        nodeEntity.setWidth(nodeDto.getWidth());
        nodeEntity.setHeight(nodeDto.getHeight());
        return nodeEntity;
    }

    private Set<FlowDefinitionEdge> convertEdges(Set<FlowEdgeDto> edges) {
        return edges.stream()
                .map(edgeDto -> convertEdge(edgeDto))
                .collect(Collectors.toSet());
    }

    private FlowDefinitionEdge convertEdge(FlowEdgeDto edgeDto) {
        var edgeEntity = FlowDefinitionEdge.withId(object, edgeDto.getId());
        edgeEntity.setSourceId(edgeDto.getSource());
        edgeEntity.setTargetId(edgeDto.getTarget());
        edgeEntity.setSourceHandleId(edgeDto.getSourceHandle());
        edgeEntity.setTargetHandleId(edgeDto.getTargetHandle());
        edgeEntity.setAnimated(edgeDto.isAnimated());
        return edgeEntity;
    }

    private String serialize(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            log.error("Failed to serialize data", e);
            return null;
        }
    }

}
