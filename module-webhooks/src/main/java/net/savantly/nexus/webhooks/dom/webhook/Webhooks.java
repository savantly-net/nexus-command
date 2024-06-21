package net.savantly.nexus.webhooks.dom.webhook;

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
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.webhooks.WebhooksModule;

@Named(WebhooksModule.NAMESPACE + ".Webhooks")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Webhooks {
    final RepositoryService repositoryService;
    final WebhookRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Webhook create(
            final Organization organization,
            @Name final String name) {
        return repositoryService.persist(Webhook.withName(organization, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Webhook> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Optional<Webhook> findById(final String id) {
        return repository.findById(id);
    }

    @Programmatic
    public List<Webhook> findByOrganization(final Organization organization) {
        return repository.findByOrganizationId(organization.getId());
    }


}
