package net.savantly.nexus.flow.dom.emailTarget;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".EmailTargets")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class EmailTargets {
    final RepositoryService repositoryService;
    final EmailTargetRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public EmailTarget create(
            final Organization organization,
            @Name final String name) {
        return repositoryService.persist(EmailTarget.withName(organization, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<EmailTarget> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<EmailTarget> findById(final String id) {
        return repository.findById(id);
    }

    @Programmatic
    public Set<EmailTarget> findByOrganizationId(String id) {
        return repository.findByOrganizationId(id);
    }

}
