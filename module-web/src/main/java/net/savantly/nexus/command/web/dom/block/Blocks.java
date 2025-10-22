package net.savantly.nexus.command.web.dom.block;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.MinLength;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.val;
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.dom.blockType.BlockType;

@Named(NexusCommandWebModule.NAMESPACE + ".Blocks")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Blocks {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final BlockRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Block create(
            final String name,
            final BlockType blockType) {
        return repositoryService.persist(Block.withRequiredFields(name, blockType));
    }

    @Programmatic
    public Block create(
            final String id,
            final String name,
            final BlockType blockType) {
        return repositoryService.persist(Block.withRequiredFields(id, name, blockType));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Block> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public Block findByName(final Block item) {
        return item;
    }
    
    @MemberSupport
    public Collection<Block> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public List<BlockDto> listAllDtos() {
        return repository.findAll().stream().map(d -> toDto(d)).collect(Collectors.toList());
    }

    @Programmatic
    public BlockDto getDtoById(final String id) {
        return toDto(findById(id));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(hidden = Where.OBJECT_FORMS)
    public Block findById(final String id) {
        return repository.getReferenceById(id);
    }

    @Programmatic
    public BlockDto updateFromDto(final BlockDto dto) throws JsonProcessingException {
        val entity = repository.getReferenceById(dto.getId());
        entity.setName(dto.getName());
        entity.setContent(dto.getContent());
        return toDto(entity);
    }


    @Programmatic
    private BlockDto toDto(Block blockType) {
        return BlockDtoConverter.toDto(blockType);
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
