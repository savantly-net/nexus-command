package domainapp.webapp.application.services.franchise.users;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.franchiseUser.FranchiseUser;
import net.savantly.franchise.dom.franchiseUser.FranchiseUsers;


@Named(FranchiseModule.NAMESPACE + ".FranchiseUsers")
@DomainService(
        nature = NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class FranchiseUserService implements FranchiseUsers {

    private static final Logger logger = LogManager.getLogger(FranchiseUserService.class);

    final private ApplicationUserRepository applicationUserRepository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    @Override
    public List<FranchiseUser> findAll() {
        var allApplicationUsers = applicationUserRepository.allUsers();
        if (allApplicationUsers == null || allApplicationUsers.isEmpty()) {
            logger.warn("No users found");
            return null;
        }
        logger.info("Found {} users", allApplicationUsers.size());
        return allApplicationUsers
                .stream().map(u -> {
                    var user = new FranchiseUser();
                    user.setUsername(u.getName());
                    user.setDisplayName(u.getName());
                    return user;
                }).collect(Collectors.toList());
    }

}
