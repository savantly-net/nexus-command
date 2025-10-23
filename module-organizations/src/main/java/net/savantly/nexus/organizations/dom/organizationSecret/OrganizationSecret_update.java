package net.savantly.nexus.organizations.dom.organizationSecret;

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

import jakarta.inject.Inject;
import net.savantly.encryption.jpa.AttributeEncryptor;

@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "secret", describedAs = "Update Secret", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class OrganizationSecret_update {

    final OrganizationSecret object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<OrganizationSecret_update> {
    }

    @Inject
    RepositoryService repositoryService;
    @Inject
    MessageService messageService;
    @Inject
    AttributeEncryptor attributeEncryptor;

    @MemberSupport
    public OrganizationSecret act(
        @ParameterLayout(named = "Secret", multiLine = 5, typicalLength = 100)
            final String secret) {

        object.setEncryptedSecret(attributeEncryptor.convertToDatabaseColumn(secret));
        return object;
    }
}
