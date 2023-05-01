package net.savantly.franchise.dom.web.pageTemplateType;



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
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".WebPageTemplateTypes")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class WebPageTemplateTypes {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final WebPageTemplateTypeRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public WebPageTemplateType create(
            @ParameterLayout(describedAs = "A memorable id for this template type") final String id,
            @ParameterLayout(describedAs = "A friendly name for this template type") @Name final String name) {
        return repositoryService.persist(WebPageTemplateType.withRequiredFields(id, name));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<WebPageTemplateType> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebPageTemplateType findByName(final WebPageTemplateType item) {
        return item;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebPageTemplateType findByNameExact(final String search) {
        return repository.findByNameContainingIgnoreCase(search).stream().findFirst().orElse(null);
    }
    
    public Collection<WebPageTemplateType> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(WebPageTemplateType.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<WebPageTemplateType> q = entityManager.get().createQuery(
                        "SELECT p FROM WebPageTemplateType p ORDER BY p.name",
                        WebPageTemplateType.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
