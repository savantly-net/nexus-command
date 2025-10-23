package net.savantly.nexus.command.franchise.organizations.service;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUsers;

@Named(FranchiseModule.NAMESPACE + ".OrganizationUsers")
@DomainService
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class FranchiseUserService implements OrganizationUsers {

    private static final Logger logger = LogManager.getLogger(FranchiseUserService.class);

    final private ApplicationUserRepository applicationUserRepository;

    @Programmatic
    @Override
    public List<OrganizationUser> findAll() {
        var allApplicationUsers = applicationUserRepository.allUsers();
        if (allApplicationUsers == null || allApplicationUsers.isEmpty()) {
            logger.warn("No users found");
            return null;
        }
        logger.info("Found {} users", allApplicationUsers.size());
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMemberOfOrganization'");
    }

    @Override
    public List<Organization> findOrganizationsByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findOrganizationsByUsername'");
    }

}
