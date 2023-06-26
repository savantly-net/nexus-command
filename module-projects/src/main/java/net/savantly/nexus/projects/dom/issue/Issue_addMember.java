package net.savantly.nexus.projects.dom.issue;

import java.util.Set;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.mixins.security.HasUsername;

import net.savantly.nexus.projects.dom.IssueMember.IssueMember;
import net.savantly.nexus.projects.dom.issueMemberRole.IssueMemberRole;


@javax.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Issue_addMember {

    final Issue issue;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Issue_addMember>{}

    @MemberSupport
    public Issue act(
            @ParameterLayout(named = "User") final HasUsername user,
            @ParameterLayout(named = "Role") final IssueMemberRole role) {
        issue.getMembers().add(IssueMember.withRequiredFields(issue, role, user));
        return issue;
    }

    @MemberSupport
    public Set<? extends HasUsername> autoComplete0Act(String search) {
        return issue.getProject().getMembers();
    }

}
