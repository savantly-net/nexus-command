package net.savantly.nexus.command.web.api.controllers;

import java.util.Optional;
import java.util.concurrent.Callable;

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
import net.savantly.nexus.command.web.dom.site.WebSiteDto;
import net.savantly.nexus.command.web.dom.site.WebSites;

@RestController
@RequestMapping("/api/sites")
@RequiredArgsConstructor
class SiteController {

    private final InteractionService interactionService;
    private final TransactionalProcessor transactionalProcessor;
    private final WebSites sites;

    @GetMapping("/{id}")
    public WebSiteDto getBlocksBySiteId(@PathVariable final String id) {
        return call("sven",
                () -> sites.findById(id)).map(s -> s.toDto())
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
