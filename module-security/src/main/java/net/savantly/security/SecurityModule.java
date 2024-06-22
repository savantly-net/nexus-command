package net.savantly.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.savantly.security.config.SecurityConfig;
import net.savantly.security.oauth.OAuth2LoginConfig;
import net.savantly.security.oauth.OauthConfigProperties;
import net.savantly.security.users.UserProviderConfiguration;

@Configuration
@Import({ UserProviderConfiguration.class, SecurityConfig.class, OauthConfigProperties.class, OAuth2LoginConfig.class })
public class SecurityModule {


}
