package net.savantly.security.oauth;

import lombok.Data;

@Data
public class OauthConfigProperties {
    
    private ResourceServerProperties resourceServer = new ResourceServerProperties();
    private OAuthLogin login = new OAuthLogin();


    /** NOT USED YET */
    @Data
    public static class ResourceServerProperties {
        private boolean enabled = false;
        private String jwtIssuerUri;
        private String jwtAudience;
        private String jwtJwkSetUri;
    }

    @Data
    public static class OAuthLogin {
        private boolean enabled = false;

        /** NOT USED YET */
        private String name = "oauth";
        private String clientId;
        private String clientSecret;
        private String issuerUri;
        private String scope = "openid profile email";
        private String grantType = "authorization_code";
        private String usernameAttribute = "preferred_username";
    }
}
