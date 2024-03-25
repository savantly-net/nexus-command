package net.savantly.nexus.organizations.dom.organization;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.OrganizationsModule;

@Named(OrganizationsModule.NAMESPACE + ".Organizations")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout()
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organizations {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final OrganizationRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Organization create(
            @Name final String name) {
        return repositoryService.persist(Organization.withName(name));
    }

    @Programmatic
    public Organization create(
            final String id,
            @Name final String name) {
        return repositoryService.persist(Organization.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Organization> listAll() {
        return repository.findAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public Organization findByName(final Organization group) {
        return group;
    }

    @Programmatic
    public Organization findByNameIgnoreCase(final String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Programmatic
    public Organization getById(String organizationId) {
        return repository.getReferenceById(organizationId);
    }

    public Collection<Organization> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(Organization.class)
                .ifSuccess(entityManager -> {
                    final TypedQuery<Organization> q = entityManager.get().createQuery(
                            "SELECT p FROM Organizations p ORDER BY p.name",
                            Organization.class)
                            .setMaxResults(1);
                    q.getResultList();
                });
    }

}
