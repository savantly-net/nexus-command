package net.savantly.nexus.flow.dom.flowSecret;

import java.util.List;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.encryption.jpa.AttributeEncryptor;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".FlowSecrets")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public final class FlowSecrets {
    final FlowSecretRepository repository;
    final AttributeEncryptor attributeEncryptor;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public FlowSecret create(
            final Organization organization,
            @Name final String name,
            final String secret) {

        var entity = FlowSecret.withName(organization, name);
        entity.setEncryptedSecret(attributeEncryptor.convertToDatabaseColumn(secret));
        return repository.save(entity);
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<FlowSecret> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public String decryptSecretString(FlowSecret secret) {
        return attributeEncryptor.convertToEntityAttribute(secret.getEncryptedSecret());
    }

}
