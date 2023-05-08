package net.savantly.nexus.command.web.dom.pageBlock;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.dom.block.Block;
import net.savantly.nexus.command.web.dom.page.WebPage;

@Named(NexusCommandWebModule.NAMESPACE + ".PageBlocks")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class PageBlocks {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final PageBlockRepository repository;

    @Programmatic
    public PageBlock create(
        final String id,
        final WebPage webPage,
        final Block block) {
    return repositoryService.persist(PageBlock.withRequiredFields(webPage, block, id));
}


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<PageBlock> listAll() {
        return repository.findAll();
    }
    
    @Programmatic
    public PageBlock findById(final String id) {
        return repository.getReferenceById(id);
    }
    

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(PageBlock.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<PageBlock> q = entityManager.get().createQuery(
                        "SELECT p FROM PageBlock p ORDER BY p.name",
                        PageBlock.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
