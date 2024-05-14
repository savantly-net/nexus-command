package net.savantly.security.users;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class UserProviderImpl implements UserProvider {

    private final String anonymousUser = "anonymous";
    private final String preferredUsernameClaim = "preferred_username";

    @Override
    public String getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return anonymousUser;
        }

        log.debug("authentication type: {}", authentication.getClass().getName());

        var principal = authentication.getPrincipal();
        if (principal == null) {
            return anonymousUser;
        }

        log.debug("principal type: {}", principal.getClass().getName());

        if (principal instanceof String) {
            return (String) principal;
        }
        if (principal instanceof OAuth2AuthenticatedPrincipal) {
            String name = ((OAuth2AuthenticatedPrincipal) principal).getName();
            return name;
        }

        if (principal instanceof JwtAuthenticationToken) {
            String name = ((JwtAuthenticationToken) principal).getTokenAttributes().get(preferredUsernameClaim)
                    .toString();
            return name;
        }

        if (principal instanceof Jwt) {
            String name = ((Jwt) principal).getClaim(preferredUsernameClaim);
            return name;
        }
        return anonymousUser;
    }

}
