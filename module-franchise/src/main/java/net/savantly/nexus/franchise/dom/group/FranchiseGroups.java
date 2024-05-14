package net.savantly.nexus.franchise.dom.group;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.franchise.dom.location.FranchiseLocation;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FranchiseModule.NAMESPACE + ".FranchiseGroups")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FranchiseGroups {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final FranchiseGroupRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public FranchiseGroup create(
            final Organization organization,
            @Name final String name) {
        return repositoryService.persist(FranchiseGroup.withName(organization, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<FranchiseGroup> listAll() {
        return repository.findAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public FranchiseGroup findByName(final FranchiseGroup group) {
        return group;
    }

    @MemberSupport
    public Collection<FranchiseGroup> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(FranchiseLocation.class)
                .ifSuccess(entityManager -> {
                    final TypedQuery<FranchiseGroup> q = entityManager.get().createQuery(
                            "SELECT p FROM FranchiseGroup p ORDER BY p.name",
                            FranchiseGroup.class)
                            .setMaxResults(1);
                    q.getResultList();
                });
    }
}
