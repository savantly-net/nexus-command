package net.savantly.nexus.projects.dom.project;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;

import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUsers;
import net.savantly.nexus.projects.dom.projectMember.ProjectMember;
import net.savantly.nexus.projects.dom.projectMemberRole.ProjectMemberRole;


@javax.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Project_addMember {

    final Project project;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_addMember>{}

    @Inject
    OrganizationUsers userRepository;

    @MemberSupport
    public Project act(
            @ParameterLayout(named = "User") final OrganizationUser user,
            @ParameterLayout(named = "Role") final ProjectMemberRole role) {
        project.getMembers().add(ProjectMember.withRequiredFields(project, role, user));
        return project;
    }

    @MemberSupport
    public List<OrganizationUser> autoComplete0Act(String search) {
        return this.userRepository.findAll().stream().collect(Collectors.toList());
    }

}
