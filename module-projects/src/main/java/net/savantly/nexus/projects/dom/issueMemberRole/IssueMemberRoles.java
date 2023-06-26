package net.savantly.nexus.projects.dom.issueMemberRole;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.BookmarkPolicy;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.projects.ProjectsModule;

@Named(ProjectsModule.NAMESPACE + ".IssueMemberRoles")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class IssueMemberRoles {
    
    private final IssueMemberRoleRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public IssueMemberRole create(
            @Name final String name) {
        return repository.save(IssueMemberRole.withRequiredFields(name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
    public List<IssueMemberRole> listAll() {
        return repository.findAll();
    }
    
}
