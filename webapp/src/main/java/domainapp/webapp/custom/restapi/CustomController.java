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
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.dom.location.FranchiseLocations;

@RestController
@RequiredArgsConstructor(onConstructor_ = {@Inject})
class CustomController {

    private final InteractionService interactionService;
    private final TransactionalProcessor transactionalProcessor;
    private final FranchiseLocations simpleObjects;

    @GetMapping("/custom/simpleObjects")
    List<FranchiseLocation> all() {
        return call("sven", simpleObjects::listAll)
                .orElse(Collections.<FranchiseLocation>emptyList());
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
