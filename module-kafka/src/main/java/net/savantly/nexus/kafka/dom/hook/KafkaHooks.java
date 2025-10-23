package net.savantly.nexus.kafka.dom.hook;

import java.util.List;
import java.util.Optional;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.kafka.KafkaModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(KafkaModule.NAMESPACE + ".KafkaHooks")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class KafkaHooks {
    final RepositoryService repositoryService;
    final KafkaHookRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public KafkaHook create(
            final Organization organization,
            @Name final String name) {
        return repositoryService.persist(KafkaHook.withName(organization, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<KafkaHook> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<KafkaHook> findById(final String id) {
        return repository.findById(id);
    }

    @Programmatic
    public List<KafkaHook> findByOrganization(final Organization organization) {
        return repository.findByOrganizationId(organization.getId());
    }

}
