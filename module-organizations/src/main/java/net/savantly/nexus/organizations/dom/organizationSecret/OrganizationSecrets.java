package net.savantly.nexus.organizations.dom.organizationSecret;

import java.util.List;
import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.encryption.jpa.AttributeEncryptor;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(OrganizationsModule.NAMESPACE + ".OrganizationSecrets")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public final class OrganizationSecrets {
    final OrganizationSecretRepository repository;
    final AttributeEncryptor attributeEncryptor;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public OrganizationSecret create(
            final Organization organization,
            @Parameter(regexPattern = "^[a-zA-Z0-9]*", regexPatternReplacement = "Must be alphanumeric or underscore") @Name final String name,
            final String secret) {

        var entity = OrganizationSecret.withName(organization, name);
        entity.setEncryptedSecret(attributeEncryptor.convertToDatabaseColumn(secret));
        return repository.save(entity);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<OrganizationSecret> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public String decryptSecretString(OrganizationSecret secret) {
        return attributeEncryptor.convertToEntityAttribute(secret.getEncryptedSecret());
    }

    public Set<OrganizationSecret> findByOrganizationId(String id) {
        return repository.findAllByOrganizationId(id);
    }

}
