package net.savantly.nexus.command.web.dom.site;



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
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.command.web.NexusCommandWebModule;

@Named(NexusCommandWebModule.NAMESPACE + ".WebSites")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class WebSites {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final WebSiteRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public WebSite create(
        @ParameterLayout(describedAs = "A friendly name for the website") final String name,
        final String id) {
    return repository.save(WebSite.withRequiredFields(name, id));
}


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<WebSite> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public WebSite findByName(final WebSite item) {
        return item;
    }

    @Programmatic
    public WebSite findById(final String id) {
        return repository.getReferenceById(id);
    }
    
    public Collection<WebSite> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(WebSite.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<WebSite> q = entityManager.get().createQuery(
                        "SELECT p FROM WebSite p ORDER BY p.name",
                        WebSite.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
