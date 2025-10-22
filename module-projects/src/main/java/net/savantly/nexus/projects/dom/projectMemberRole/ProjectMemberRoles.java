package net.savantly.nexus.projects.dom.projectMemberRole;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.projects.ProjectsModule;

@Named(ProjectsModule.NAMESPACE + ".ProjectMemberRoles")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ProjectMemberRoles {

    private final ProjectMemberRoleRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public ProjectMemberRole create(
            @Name final String name) {
        return repository.save(ProjectMemberRole.withRequiredFields(name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<ProjectMemberRole> listAll() {
        return repository.findAll();
    }

}
