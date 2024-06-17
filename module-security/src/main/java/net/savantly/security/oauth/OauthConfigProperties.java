package net.savantly.security.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "nexus.security.oauth2")
public class OauthConfigProperties {
    
    private ResourceServerProperties resourceServer = new ResourceServerProperties();
    private OAuthLogin login = new OAuthLogin();


    @Data
    public static class ResourceServerProperties {
        private boolean enabled = false;

        /** NOT USED YET */
        private String jwtIssuerUri;
        private String jwtAudience;
        private String jwtJwkSetUri;
    }

    @Data
    public static class OAuthLogin {
        private boolean enabled = false;

        private String name = "oauth2";
        private String clientId;
        private String clientSecret;
        private String scope = "openid profile email";
        //private String grantType = "authorization_code";
        private String usernameAttribute = "preferred_username";

        private String issuerUri;
        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
        private String jwkSetUri;
        private String registrationId = "oauth2";
    }
}
