package net.savantly.franchise.dom.web.blockType;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.val;
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.types.Name;

@Named(FranchiseModule.NAMESPACE + ".BlockTypes")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class BlockTypes {
	
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final BlockTypeRepository repository;
    final ObjectMapper objectMapper = new ObjectMapper();


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public BlockType create(
            @Name final String name) {
        return repositoryService.persist(BlockType.withRequiredFields(name));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<BlockType> listAll() {
        return repository.findAll();
    }
    
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
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
    public BlockTypeDto getById(final Long id) {
        return toDto(repository.getReferenceById(id));
    }

    @Programmatic
    public BlockTypeDto updateFromDto(final BlockTypeDto dto) throws JsonProcessingException {
        val entity = repository.getReferenceById(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setSchema(asString(dto.getSchema()));
        entity.setUiSchema(asString(dto.getUiSchema()));
        return toDto(entity);
    }

    @Programmatic
    private BlockTypeDto toDto(BlockType blockType) {
        val dto = new BlockTypeDto();
        dto.setId(blockType.getId());
        dto.setName(blockType.getName());
        dto.setDescription(blockType.getDescription());
        dto.setSchema(asMap(blockType.getSchema()));
        dto.setUiSchema(asMap(blockType.getUiSchema()));
        return dto;
    }


    private Map<String, Object> asMap(String dataString) {
        return objectMapper.convertValue(dataString, Map.class);
    }
    
    private String asString(Map<String, Object> data) throws JsonProcessingException {
        return objectMapper.writeValueAsString(data);
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
