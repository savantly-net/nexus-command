package net.savantly.security.preauthenticated;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import net.savantly.security.model.SecurityUserModel;

/**
 * This class represents a pre-authentication filter that extends the
 * AbstractPreAuthenticatedProcessingFilter class.
 * It is responsible for extracting user information from the request headers
 * and creating a UserDto object with the extracted information.
 * The UserDto object is then used as the principal for authentication.
 */
@Log4j2
public class PreAuthenticatedFilter extends AbstractPreAuthenticatedProcessingFilter {

    private final PreAuthConfigProperties preauth;

    public PreAuthenticatedFilter(PreAuthConfigProperties preauth, AuthenticationManager authenticationManager) {
        this.preauth = preauth;
        this.setAuthenticationManager(authenticationManager);
        this.setAuthenticationSuccessHandler((request, response, authentication) -> {
            authentication.setAuthenticated(true);
        });
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        try {
            String uid = request.getHeader(preauth.getUserIdHeaderName());

            if (uid == null) {
                log.debug("No user id header found");
                return null;
            }

            String username = request.getHeader(preauth.getUsernameHeaderName());
            String email = request.getHeader(preauth.getEmailHeaderName());

            String groupsString = request.getHeader(preauth.getGroupsHeaderName());

            var groups = parseGroups(groupsString);

            var dto = new SecurityUserModel()
                    .setUid(uid)
                    .setEmail(email)
                    .setUsername(username)
                    .setGroups(groups);

            return dto;

        } catch (Exception e) {
            log.error("Error parsing preauth headers", e);
            return null;
        }
    }

    private List<String> parseGroups(String groups) {
        if (groups == null) {
            return List.of();
        }
        return List.of(groups.split(","));
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return "N/A";
    }

}
