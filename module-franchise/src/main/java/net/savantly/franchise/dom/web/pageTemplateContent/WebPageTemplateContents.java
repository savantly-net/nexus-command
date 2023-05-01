package net.savantly.franchise.dom.web.pageTemplateContent;


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
import net.savantly.franchise.dom.web.pageTemplate.WebPageTemplate;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".WebPageTemplateContents")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class WebPageTemplateContents {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final WebPageTemplateContentRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public WebPageTemplateContent create(
            @Name final String name,
            final WebPageTemplate template) {
        return repositoryService.persist(WebPageTemplateContent.withRequiredFields(name, template));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<WebPageTemplateContent> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebPageTemplateContent findByName(final WebPageTemplateContent item) {
        return item;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebPageTemplateContent findByNameExact(final String search) {
        return repository.findByNameContainingIgnoreCase(search).stream().findFirst().orElse(null);
    }
    
    public Collection<WebPageTemplateContent> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(WebPageTemplateContent.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<WebPageTemplateContent> q = entityManager.get().createQuery(
                        "SELECT p FROM WebPageTemplateContent p ORDER BY p.name",
                        WebPageTemplateContent.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
