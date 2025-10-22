package net.savantly.nexus.command.web.dom.page;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.MinLength;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.dom.site.WebSite;

@Named(NexusCommandWebModule.NAMESPACE + ".WebPages")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class WebPages {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final WebPageRepository repository;

    @Programmatic
    public WebPage create(
        final String id,
        final String name,
        final WebSite site) {
        return repositoryService.persist(WebPage.withRequiredFields(name, site, id));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<WebPage> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public WebPage findByName(final WebPage item) {
        return item;
    }

    @Programmatic
    public WebPage findById(final String id) {
        return repository.getReferenceById(id);
    }
    
    @MemberSupport
    public Collection<WebPage> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(WebPage.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<WebPage> q = entityManager.get().createQuery(
                        "SELECT p FROM WebPage p ORDER BY p.name",
                        WebPage.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
