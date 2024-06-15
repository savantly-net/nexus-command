package net.savantly.nexus.flow.dom.connections.jdbcConnection;

import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowSecret.FlowSecret;
import net.savantly.nexus.flow.dom.flowSecret.FlowSecrets;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "destinations", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class JdbcConnection_updatePassword {

    final JdbcConnection object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<JdbcConnection_updatePassword> {
    }

    @Inject
    MessageService messageService;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    FlowSecrets flowSecrets;

    @MemberSupport
    public JdbcConnection act(final FlowSecret secret) {
        try {
            object.setPassword(secret);
            messageService.informUser("Updated Secret Reference");

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

    @MemberSupport
    public FlowSecret default0Act() {
        return object.getPassword();
    }

    @MemberSupport
    public Set<FlowSecret> choices0Act() {
        return flowSecrets.findByOrganizationId(object.getOrganization().getId());
    }

}
