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
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecret;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;

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
    OrganizationSecrets flowSecrets;

    @MemberSupport
    public JdbcConnection act(final OrganizationSecret secret) {
        try {
            object.setPassword(secret);
            messageService.informUser("Updated Secret Reference");

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

    @MemberSupport
    public OrganizationSecret default0Act() {
        return object.getPassword();
    }

    @MemberSupport
    public Set<OrganizationSecret> choices0Act() {
        return flowSecrets.findByOrganizationId(object.getOrganization().getId());
    }

}
