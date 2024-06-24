package net.savantly.security.oauth;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "nexus.security.oauth2")
public class OAuth2ConfigProperties {
    
    private ResourceServerProperties resourceServer = new ResourceServerProperties();
    private OAuthLogin login = new OAuthLogin();
    private Roles roles = new Roles();

    @Data
    public static class Roles {
        private List<String> sticky = List.of("application-user");

        /**
         * JsonPath expression to extract roles from the JWT token
         */
        private String claim = "$.roles";

        /**
         * Map roles from the claim to roles in the application
         */
        private Map<String, String> mappings = Map.of("admin", "causeway-ext-secman-admin");

        /**
         * Remove all roles from the user that are not in the claim
         */
        private boolean removeMissing = true;
    }


    @Data
    public static class ResourceServerProperties {

        /**
         * Enable the resource server authorization
         */
        private boolean enabled = false;

        /** NOT USED YET */
        private String jwtIssuerUri;
        private String jwtAudience;
        private String jwtJwkSetUri;
    }

    @Data
    public static class OAuthLogin {
        /**
         * Enable the OAuth2 login
         */
        private boolean enabled = false;

        /**
         * The name of the OAuth2 provider
         */
        private String name = "oauth2";

        /**
         * The client id
         */
        private String clientId;

        /**
         * The client secret
         */
        private String clientSecret;

        /**
         * The scope of the OAuth2 login
         */
        private String scope = "openid profile email roles";
        //private String grantType = "authorization_code";

        /**
         * The claim that contains the username
         */
        private String usernameAttribute = "preferred_username";

        private String issuerUri;
        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
        private String jwkSetUri;
        private String registrationId = "oauth2";
    }
}
