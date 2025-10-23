package net.savantly.nexus.franchise.dom.group;


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

import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUsers;


@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class FranchiseGroup_addMember {

    final FranchiseGroup franchiseGroup;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FranchiseGroup_addMember>{}

    @Inject
    OrganizationUsers userRepository;

    @MemberSupport
    public FranchiseGroup act(
            @ParameterLayout(named = "User") final OrganizationUser user,
            @ParameterLayout(named = "Role") final FranchiseGroupMemberRole role) {
        franchiseGroup.getMembers().add(FranchiseGroupMember.withRequiredFields(franchiseGroup, role, user));
        return franchiseGroup;
    }

    @MemberSupport
    public List<OrganizationUser> autoComplete0Act(String search) {
        return this.userRepository.findAll().stream().collect(Collectors.toList());
    }

}
