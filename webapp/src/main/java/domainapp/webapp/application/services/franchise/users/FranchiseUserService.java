package domainapp.webapp.application.services.franchise.users;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUsers;


@Named(FranchiseModule.NAMESPACE + ".OrganizationUsers")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
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

}
