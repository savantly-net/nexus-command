package net.savantly.franchise.dom.web.block;


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
import net.savantly.franchise.dom.web.blockType.BlockType;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".Blocks")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Blocks {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final BlockRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Block create(
            @Name final String name,
            final BlockType blockType) {
        return repositoryService.persist(Block.withRequiredFields(name, blockType));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<Block> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public Block findByName(final Block item) {
        return item;
    }
    
    public Collection<Block> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(Block.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<Block> q = entityManager.get().createQuery(
                        "SELECT p FROM Block p ORDER BY p.name",
                        Block.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
