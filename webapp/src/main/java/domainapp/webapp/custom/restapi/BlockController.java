package domainapp.webapp.custom.restapi;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.inject.Inject;

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
import net.savantly.nexus.command.web.dom.block.BlockDto;
import net.savantly.nexus.command.web.dom.block.Blocks;

@RestController
@RequestMapping("/api/blocks")
@RequiredArgsConstructor(onConstructor_ = {@Inject})
class BlockController {

    private final InteractionService interactionService;
    private final TransactionalProcessor transactionalProcessor;
    private final Blocks blocks;

    @GetMapping
    public List<BlockDto> allBlocks() {
        return call("sven", blocks::listAllDtos)
                .orElse(Collections.<BlockDto>emptyList());
    }

    @GetMapping("/{id}")
    public BlockDto getBlockById(@PathVariable final String id) {
        return call("sven", () -> blocks.getDtoById(id))
                .orElse(null);
    }

    @PostMapping("/update")
    public BlockDto updateBlock(@RequestBody final BlockDto blockDto) {
        return call("sven", () -> blocks.updateFromDto(blockDto))
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
