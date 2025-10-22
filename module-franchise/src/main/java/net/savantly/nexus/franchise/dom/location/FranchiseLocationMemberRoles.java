package net.savantly.nexus.franchise.dom.location;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.franchise.FranchiseModule;

@Named(FranchiseModule.NAMESPACE + ".FranchiseLocationMemberRoles")
@DomainService(
        
)
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class FranchiseLocationMemberRoles {
    
    private final FranchiseLocationMemberRoleRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public FranchiseLocationMemberRole create(
            @Name final String name) {
        return repository.save(FranchiseLocationMemberRole.withRequiredFields(name));
    }


    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<FranchiseLocationMemberRole> listAll() {
        return repository.findAll();
    }
    
}
