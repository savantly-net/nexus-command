package net.savantly.nexus.command.web.dom.blockType;

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
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.MinLength;

import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.val;
import net.savantly.nexus.command.web.NexusCommandWebModule;

@Named(NexusCommandWebModule.NAMESPACE + ".BlockTypes")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class BlockTypes {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final BlockTypeRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public BlockType create(
        final String id,
        final String name) {
        return repositoryService.persist(BlockType.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<BlockType> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public BlockType findByName(final BlockType item) {
        return item;
    }

    public Collection<BlockType> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public List<BlockTypeDto> listAllDtos() {
        return repository.findAll().stream().map(d -> toDto(d)).collect(Collectors.toList());
    }

    @Programmatic
    public BlockTypeDto getDtoById(final String id) {
        return toDto(getById(id));
    }

    @Programmatic
    public BlockType getById(final String id) {
        return repository.getReferenceById(id);
    }

    @Programmatic
    public BlockTypeDto updateFromDto(final BlockTypeDto dto) throws JsonProcessingException {
        val entity = repository.getReferenceById(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSchema(dto.getSchema());
        entity.setUiSchema(dto.getUiSchema());
        return toDto(entity);
    }

    @Programmatic
    private BlockTypeDto toDto(BlockType blockType) {
        val dto = new BlockTypeDto();
        dto.setId(blockType.getId());
        dto.setName(blockType.getName());
        dto.setDescription(blockType.getDescription());
        dto.setSchema(blockType.getSchema());
        dto.setUiSchema(blockType.getUiSchema());
        return dto;
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(BlockType.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<BlockType> q = entityManager.get().createQuery(
                        "SELECT p FROM BlockType p ORDER BY p.name",
                        BlockType.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
