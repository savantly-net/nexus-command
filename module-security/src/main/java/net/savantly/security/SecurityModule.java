package net.savantly.security;

import java.util.List;

import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.extensions.secman.applib.role.dom.ApplicationRoleRepository;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.savantly.security.config.SecurityConfig;
import net.savantly.security.listener.AppUserAutoCreationService;
import net.savantly.security.oauth.OAuth2LoginConfig;
import net.savantly.security.oauth.OauthConfigProperties;
import net.savantly.security.users.UserProviderConfiguration;

@Configuration
@Import({ UserProviderConfiguration.class, SecurityConfig.class, OauthConfigProperties.class, OAuth2LoginConfig.class })
public class SecurityModule {


    @Bean
    public AppUserAutoCreationService appUserAutoCreationService(OauthConfigProperties securityConfigProps,
            ApplicationUserRepository userRepo, ApplicationRoleRepository roleRepo, FactoryService factoryService) {
        return new AppUserAutoCreationService(securityConfigProps, userRepo, roleRepo, factoryService);
    }

}
