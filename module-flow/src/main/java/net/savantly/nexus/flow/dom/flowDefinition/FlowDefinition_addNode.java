package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
import net.savantly.nexus.flow.dom.flowDefinitionNode.FlowDefinitionNode;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDiscoveryService;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "nodes", describedAs = "Add Node", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FlowDefinition_addNode {

    final FlowDefinition object;
    final ObjectMapper objectMapper = new ObjectMapper();

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FlowDefinition_addNode> {
    }

    @Inject
    RepositoryService repositoryService;
    @Inject
    MessageService  messageService;
    @Inject
    FlowNodeDiscoveryService flowNodeDiscoveryService;

    @MemberSupport
    public FlowDefinitionNode act(final String nodeType) {

        var node = FlowDefinitionNode.withId(object, UUID.randomUUID().toString().substring(0, 8));
        node.setNodeType(nodeType);
        object.addNode(node);
        return node;
    }

    @MemberSupport
    public Set<String> choices0Act() {
        return flowNodeDiscoveryService.discoverFlowNodes().stream().map(n -> n.getName()).collect(Collectors.toSet());
    }

}
