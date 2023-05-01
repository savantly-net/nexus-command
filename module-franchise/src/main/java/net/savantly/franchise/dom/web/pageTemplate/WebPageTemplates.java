package net.savantly.franchise.dom.web.pageTemplate;

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
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".WebPageTemplates")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class WebPageTemplates {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final WebPageTemplateRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public WebPageTemplate create(
            @Name final String name) {
        return repositoryService.persist(WebPageTemplate.withRequiredFields(name));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<WebPageTemplate> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebPageTemplate findByName(final WebPageTemplate item) {
        return item;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebPageTemplate findByNameExact(final String search) {
        return repository.findByNameContainingIgnoreCase(search).stream().findFirst().orElse(null);
    }
    
    public Collection<WebPageTemplate> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(WebPageTemplate.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<WebPageTemplate> q = entityManager.get().createQuery(
                        "SELECT p FROM WebPageTemplate p ORDER BY p.name",
                        WebPageTemplate.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
