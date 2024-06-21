package net.savantly.nexus.organizations.dom.organizationUser;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organizationMember.OrganizationMember;
import net.savantly.nexus.organizations.dom.organizationMember.OrganizationMemberRepository;

@Log4j2
@Named(OrganizationsModule.NAMESPACE + ".OrganizationUsers")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class DefaultOrganizationUsers implements OrganizationUsers {

    final private ApplicationUserRepository applicationUserRepository;
    final private OrganizationMemberRepository repository;

    @Programmatic
    @Override
    public List<OrganizationUser> findAll() {
        var allApplicationUsers = applicationUserRepository.allUsers();
        if (allApplicationUsers == null || allApplicationUsers.isEmpty()) {
            log.warn("No users found");
            return null;
        }
        log.info("Found {} users", allApplicationUsers.size());
        return allApplicationUsers
                .stream().map(u -> {
                    var user = new OrganizationUser();
                    user.setUsername(u.getName());
                    user.setDisplayName(u.getName());
                    return user;
                }).collect(Collectors.toList());
    }

    @Override
    public boolean isMemberOfOrganization(String username, Organization organization) {
        Set<OrganizationMember> membership = repository.findByUsernameAndOrganizationId(username, organization.getId());
        return membership != null && !membership.isEmpty();
    }

    @Override
    public List<Organization> findOrganizationsByUsername(String username) {
        Set<OrganizationMember> memberships = repository.findByUsername(username);
        return memberships.stream().map(m -> m.getOrganization()).collect(Collectors.toList());
    }


}
