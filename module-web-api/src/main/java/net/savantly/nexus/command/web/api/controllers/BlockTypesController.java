package net.savantly.nexus.command.web.api.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import jakarta.inject.Inject;

import org.apache.causeway.applib.services.iactnlayer.InteractionContext;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.user.UserMemento;
import org.apache.causeway.applib.services.xactn.TransactionalProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.command.web.dom.blockType.BlockTypeDto;
import net.savantly.nexus.command.web.dom.blockType.BlockTypes;

@RestController
@RequestMapping("/api/block-types")
@RequiredArgsConstructor
class BlockTypesController {

    private final InteractionService interactionService;
    private final TransactionalProcessor transactionalProcessor;
    private final BlockTypes blockTypes;

    @GetMapping
    public List<BlockTypeDto> allBlockTypes() {
        return call("sven", blockTypes::listAllDtos)
                .orElse(Collections.<BlockTypeDto>emptyList());
    }

    @GetMapping("/{id}")
    public BlockTypeDto allBlockTypeById(@PathVariable final String id) {
        return call("sven", () -> blockTypes.getDtoById(id))
                .orElse(null);
    }

    @PostMapping("/update")
    public BlockTypeDto updateBlock(@RequestBody final BlockTypeDto blockDto) {
        return call("sven", () -> blockTypes.updateFromDto(blockDto))
                .orElse(null);
    }

    private <T> Optional<T> call(
            final String username,
            final Callable<T> callable) {

        return interactionService.call(
                InteractionContext.ofUserWithSystemDefaults(UserMemento.ofName(username)),
                () -> transactionalProcessor.callWithinCurrentTransactionElseCreateNew(callable))
                .ifFailureFail().getValue();
    }

}
