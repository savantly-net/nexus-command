package net.savantly.nexus.flow.dom.flowDefinition;


import java.util.Set;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowDefinitionEdge.FlowDefinitionEdge;
import net.savantly.nexus.flow.dom.flowDefinitionNode.FlowDefinitionNode;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "edges", describedAs = "Add Edge", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FlowDefinition_addEdge {

    final FlowDefinition object;
    final ObjectMapper objectMapper = new ObjectMapper();

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FlowDefinition_addEdge> {
    }

    @Inject
    RepositoryService repositoryService;
    @Inject
    MessageService  messageService;

    @MemberSupport
    public FlowDefinition act(
        final FlowDefinitionNode sourceNode,
        final FlowDefinitionNode targetNode) 
    {

        var edge = FlowDefinitionEdge.withId(object, UUID.randomUUID().toString().substring(0, 8));
        edge.setSourceId(sourceNode.getId());
        edge.setTargetId(targetNode.getId());
        object.addEdge(edge);
        return object;
    }

    @MemberSupport
    public Set<FlowDefinitionNode> choices0Act() {
        return object.getNodes();
    }

    @MemberSupport
    public Set<FlowDefinitionNode> choices1Act() {
        return object.getNodes();
    }

}
