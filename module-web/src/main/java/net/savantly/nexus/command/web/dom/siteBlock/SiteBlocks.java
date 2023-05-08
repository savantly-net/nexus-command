package net.savantly.nexus.command.web.dom.siteBlock;

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
import net.savantly.nexus.command.web.dom.site.WebSite;

@Named(NexusCommandWebModule.NAMESPACE + ".SiteBlocks")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class SiteBlocks {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final SiteBlockRepository repository;

    @Programmatic
    public SiteBlock create(
        final String id,
        final WebSite webSite,
        final Block block) {
    return repositoryService.persist(SiteBlock.withRequiredFields(webSite, block, id));
}


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<SiteBlock> listAll() {
        return repository.findAll();
    }
    
    @Programmatic
    public SiteBlock findById(final String id) {
        return repository.getReferenceById(id);
    }
    
    @Programmatic
    public List<SiteBlock> findBySiteId(final String siteId) {
        return repository.findBySiteId(siteId);
    }

    @Programmatic
    public List<SiteBlock> findBySiteIdAndBlockTypeId(final String siteId, final String blockTypeId) {
        return repository.findBySiteIdAndBlockBlockTypeId(siteId, blockTypeId);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(SiteBlock.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<SiteBlock> q = entityManager.get().createQuery(
                        "SELECT p FROM SiteBlock p ORDER BY p.name",
                        SiteBlock.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
