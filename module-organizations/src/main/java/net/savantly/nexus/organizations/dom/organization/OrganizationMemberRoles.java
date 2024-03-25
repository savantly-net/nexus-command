package net.savantly.nexus.organizations.dom.organization;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.OrganizationsModule;

@Named(OrganizationsModule.NAMESPACE + ".OrganizationMemberRoles")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout()
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class OrganizationMemberRoles {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final OrganizationMemberRoleRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public OrganizationMemberRole create(
            @Name final String name) {
        return repositoryService.persist(OrganizationMemberRole.withName(name));
    }

    @Programmatic
    public OrganizationMemberRole create(
            final String id,
            @Name final String name) {
        return repositoryService.persist(OrganizationMemberRole.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<OrganizationMemberRole> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(OrganizationMemberRole.class)
                .ifSuccess(entityManager -> {
                    final TypedQuery<OrganizationMemberRole> q = entityManager.get().createQuery(
                            "SELECT p FROM OrganizationMemberRole p ORDER BY p.name",
                            OrganizationMemberRole.class)
                            .setMaxResults(1);
                    q.getResultList();
                });
    }

}
