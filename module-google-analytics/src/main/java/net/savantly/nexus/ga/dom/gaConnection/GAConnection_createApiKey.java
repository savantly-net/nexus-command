package net.savantly.nexus.ga.dom.gaConnection;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.encryption.jpa.AttributeEncryptor;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecret;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "apiKey", named = "Create Secret", describedAs = "Update the API Key with a new secret")
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GAConnection_createApiKey {

    final GAConnection object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<GAConnection_updateApiKey> {
    }

    @Inject
    MessageService messageService;
    @Inject
    OrganizationSecrets secrets;
    @Inject
    AttributeEncryptor attributeEncryptor;
    @Inject
    RepositoryService repositoryService;

    @MemberSupport
    public GAConnection act(final String apiKey) {
        try {
            var secret = OrganizationSecret.withName(object.getOrganization(), object.getName());
            secret.setEncryptedSecret(attributeEncryptor.convertToDatabaseColumn(apiKey));
            repositoryService.persist(secret);
            object.setApiKey(secret);
            messageService.informUser("Updated Secret");

        } catch (Exception e) {
            messageService.raiseError("Failed to create secret: " + e.getMessage());
        }

        return object;
    }

}
