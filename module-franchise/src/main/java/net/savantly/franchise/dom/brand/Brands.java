package net.savantly.franchise.dom.brand;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
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

import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".Brands")
@DomainService(
        nature = NatureOfService.VIEW
)
@DomainServiceLayout()
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Brands {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final BrandRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Brand create(
            @Name final String name) {
        return repositoryService.persist(Brand.withName(name));
    }

    @Programmatic
    public Brand create(
            final String id,
            @Name final String name) {
        return repositoryService.persist(Brand.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Brand> listAll() {
        return repository.findAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public Brand findByName(final Brand group) {
        return group;
    }

    @Programmatic
    public Brand findByNameIgnoreCase(final String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Programmatic
    public Brand getById(String brandId) {
        return repository.getReferenceById(brandId);
    }
    
    public Collection<Brand> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(FranchiseLocation.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<Brand> q = entityManager.get().createQuery(
                        "SELECT p FROM Brand p ORDER BY p.name",
                        Brand.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
