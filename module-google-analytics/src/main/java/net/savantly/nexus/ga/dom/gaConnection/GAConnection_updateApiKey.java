package net.savantly.nexus.ga.dom.gaConnection;

import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecret;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "apiKey", named = "Choose Secret", describedAs = "Update the API Key with an existing secret")
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GAConnection_updateApiKey {

    final GAConnection object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<GAConnection_updateApiKey> {
    }

    @Inject
    MessageService messageService;
    @Inject
    OrganizationSecrets secrets;

    @MemberSupport
    public GAConnection act(final OrganizationSecret secret) {
        try {
            object.setApiKey(secret);
            messageService.informUser("Updated Secret Reference");

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

    @MemberSupport
    public OrganizationSecret default0Act() {
        return object.getApiKey();
    }

    @MemberSupport
    public Set<OrganizationSecret> choices0Act() {
        return secrets.findByOrganizationId(object.getOrganization().getId());
    }

}
