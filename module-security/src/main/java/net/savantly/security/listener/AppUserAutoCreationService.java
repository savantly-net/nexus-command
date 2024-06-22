package net.savantly.security.listener;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.extensions.secman.applib.role.dom.ApplicationRole;
import org.apache.causeway.extensions.secman.applib.role.dom.ApplicationRoleRepository;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserStatus;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_addRole;
import org.apache.causeway.extensions.secman.applib.user.dom.mixins.ApplicationUser_updateEmailAddress;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.log4j.Log4j2;
import net.savantly.security.oauth.OauthConfigProperties;

@RequiredArgsConstructor(onConstructor_ = { @Inject })
@Log4j2
public class AppUserAutoCreationService
        implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final OauthConfigProperties oauthConfigProperties;
    private final ApplicationUserRepository applicationUserRepository;
    private final ApplicationRoleRepository applicationRoleRepository;
    private final FactoryService factoryService;

    @Override
    public void onApplicationEvent(@NonNull final InteractiveAuthenticationSuccessEvent event) {
        log.info("handling InteractiveAuthenticationSuccessEvent");

        var mmc = MetaModelContext.instanceElseFail();
        var interactionService = mmc.getInteractionService();

        try {

            val authentication = event.getAuthentication();
            val principal = authentication.getPrincipal();
            if (!(principal instanceof OidcUser)) {
                log.debug("not an instance of OidcUser");
                return;
            }

            val oidcUser = (DefaultOidcUser) principal;
            val username = oidcUser.getPreferredUsername();
            log.info("upserting delegated account for {}", oidcUser);
            if (Objects.isNull(username) || username.isEmpty()) {
                log.error("oidcUser has no preferredUsername: " + oidcUser.getSubject());
                return;
            }
            val email = oidcUser.getEmail();
            val givenName = Objects.nonNull(oidcUser.getGivenName()) ? oidcUser.getGivenName() : email;
            val familyName = Objects.nonNull(oidcUser.getFamilyName()) ? oidcUser.getFamilyName() : "";
            val rolesFromClaim = extractRolesFromClaim(oidcUser);
            val rolesFromAuthorities = oidcUser.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .toList();

            val allUserRoles = new HashSet<String>();
            allUserRoles.addAll(rolesFromClaim);
            allUserRoles.addAll(rolesFromAuthorities);

            val mappings = oauthConfigProperties.getRoles().getMappings();
            log.debug("role mappings: {}", mappings);
            for (String role : allUserRoles) {
                log.debug("checking role: {}", role);
                if (mappings.containsKey(role)) {
                    val mappedRole = mappings.get(role);
                    log.debug("mapping role: {} to {}", role, mappedRole);
                    allUserRoles.add(mappedRole);
                }
            }

            interactionService.runAnonymous(() -> {
                Optional<ApplicationUser> userIfAny = applicationUserRepository.findByUsername(username);
                ApplicationUser applicationUser = null;
                if (userIfAny.isEmpty()) {
                    val status = ApplicationUserStatus.UNLOCKED; // locking not supported for spring delegated accounts
                    applicationUser = applicationUserRepository.newDelegateUser(username, status);

                } else {
                    applicationUser = userIfAny.get();
                }

                factoryService.mixin(ApplicationUser_updateEmailAddress.class, applicationUser).act(email);
                applicationUser.setFamilyName(familyName);
                applicationUser.setGivenName(givenName);
                log.info("adding OidcUser roles: {}", allUserRoles);
                for (String authority : allUserRoles) {
                    addRoleIfExists(applicationUser, authority);
                }

                removeOldRoleAssignments(applicationUser, allUserRoles);

                // Add the sticky roles if they are not already assigned
                var stickyRoles = oauthConfigProperties.getRoles().getSticky();
                log.debug("adding sticky roles: {}", stickyRoles);
                for (String stickyRole : stickyRoles) {
                    addRoleIfExists(applicationUser, stickyRole);
                }

            });
        } catch (Exception e) {
            log.error("failed to sync user", e);
        }
    }

    private List<String> extractRolesFromClaim(DefaultOidcUser oidcUser) {
        Map<String, Object> claims = oidcUser.getClaims();
        if (claims.containsKey(oauthConfigProperties.getRoles().getClaim())) {
            return oidcUser.getClaimAsStringList(oauthConfigProperties.getRoles().getClaim());
        }
        return List.of();
    }

    private void removeOldRoleAssignments(ApplicationUser applicationUser,
            Collection<String> authorities) {
        // check each role the user currently has assigned
        var rolesToRemove = new HashSet<ApplicationRole>();
        applicationUser.getRoles().forEach(role -> {
            // if the role is not in the current authority list from oidc, then remove it
            if (false == authorities.stream().anyMatch(a -> a.contentEquals(role.getName()))) {
                rolesToRemove.add(role);
            }
        });

        for (ApplicationRole applicationRole : rolesToRemove) {
            applicationUser.getRoles().remove(applicationRole);
        }
    }

    private void addRoleIfExists(ApplicationUser applicationUser, String roleName) {
        var existingRoles = applicationUser.getRoles();
        if (existingRoles.stream().anyMatch(r -> r.getName().contentEquals(roleName))) {
            return;
        }
        applicationRoleRepository.findByName(roleName).ifPresent(role -> {
            factoryService.mixin(ApplicationUser_addRole.class, applicationUser).act(role);
        });
    }

}
