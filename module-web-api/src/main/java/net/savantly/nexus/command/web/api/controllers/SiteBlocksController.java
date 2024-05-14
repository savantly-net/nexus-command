package net.savantly.nexus.command.web.api.controllers;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.apache.causeway.applib.services.iactnlayer.InteractionContext;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.user.UserMemento;
import org.apache.causeway.applib.services.xactn.TransactionalProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.command.web.dom.block.BlockDto;
import net.savantly.nexus.command.web.dom.block.BlockDtoConverter;
import net.savantly.nexus.command.web.dom.siteBlock.SiteBlocks;

@RestController
@RequestMapping("/api/site-blocks")
@RequiredArgsConstructor(onConstructor_ = { @Inject })
class SiteBlocksController {

    private final InteractionService interactionService;
    private final TransactionalProcessor transactionalProcessor;
    private final SiteBlocks blocks;

    @GetMapping("/by-site/{id}")
    public List<BlockDto> getBlocksBySiteId(@PathVariable final String id) {
        return call("sven",
                () -> blocks.findBySiteId(id).stream().map(b -> BlockDtoConverter.toDto(b.getBlock()))
                        .collect(Collectors.toList()))
                .orElse(null);
    }

    @GetMapping("/by-site/{id}/type/{type}")
    public List<BlockDto> getBlocksBySiteIdAndTypeId(@PathVariable final String id, @PathVariable final String typeId) {
        return call("sven", () -> blocks.findBySiteIdAndBlockTypeId(id, typeId).stream().map(b -> {
            return BlockDtoConverter.toDto(b.getBlock());
        }).collect(Collectors.toList()))
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
