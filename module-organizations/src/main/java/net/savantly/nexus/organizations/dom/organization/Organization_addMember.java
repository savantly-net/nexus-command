package net.savantly.nexus.organizations.dom.organization;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;

import net.savantly.nexus.organizations.dom.organizationMember.OrganizationMember;
import net.savantly.nexus.organizations.dom.organizationMember.OrganizationMemberRole;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUsers;


@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Organization_addMember {

    final Organization organization;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_addMember>{}

    @Inject
    OrganizationUsers userRepository;

    @MemberSupport
    public Organization act(
            @ParameterLayout(named = "User") final OrganizationUser user,
            @ParameterLayout(named = "Role") final OrganizationMemberRole role) {
        organization.getMembers().add(OrganizationMember.withRequiredFields(organization, role, user));
        return organization;
    }

    @MemberSupport
    public List<OrganizationUser> autoComplete0Act(String search) {
        return this.userRepository.findAll().stream().collect(Collectors.toList());
    }

}
