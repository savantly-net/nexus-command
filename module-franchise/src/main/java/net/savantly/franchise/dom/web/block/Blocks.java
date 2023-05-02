package net.savantly.franchise.dom.web.block;


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
    final ObjectMapper objectMapper = new ObjectMapper();


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
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<BlockDto> listAllDtos() {
        return repository.findAll().stream().map(d -> toDto(d)).collect(Collectors.toList());
    }

    @Programmatic
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public BlockDto getById(final Long id) {
        return toDto(repository.getReferenceById(id));
    }

    @Programmatic
    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public BlockDto updateFromDto(final BlockDto dto) throws JsonProcessingException {
        val entity = repository.getReferenceById(dto.getId());
        entity.setName(dto.getName());
        entity.setContent(asString(dto.getContent()));
        return toDto(entity);
    }


    @Programmatic
    private BlockDto toDto(Block blockType) {
        val dto = new BlockDto();
        dto.setId(blockType.getId());
        dto.setName(blockType.getName());
        dto.setContent(asMap(blockType.getContent()));
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
