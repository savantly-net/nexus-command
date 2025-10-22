package net.savantly.nexus.franchise.dom.market;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.franchise.FranchiseModule;

@Named(FranchiseModule.NAMESPACE + ".FranchiseMarkets")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FranchiseMarkets {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final FranchiseMarketRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public FranchiseMarket create(
            final String id,
            @Name final String name) {
        return repositoryService.persist(FranchiseMarket.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<FranchiseMarket> listAll() {
        return repository.findAll();
    }

}
