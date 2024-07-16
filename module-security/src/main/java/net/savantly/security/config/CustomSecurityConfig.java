package net.savantly.security.config;

import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.extensions.secman.applib.role.dom.ApplicationRoleRepository;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.causeway.extensions.spring.security.oauth2.CausewayModuleExtSpringSecurityOAuth2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.savantly.security.jwt.ProxyBearerTokenResolver;
import net.savantly.security.listener.AppUserAutoCreationService;
import net.savantly.security.oauth.LoginForwardingController;
import net.savantly.security.oauth.OAuth2ConfigProperties;
import net.savantly.security.preauthenticated.PreAuthConfigProperties;

/**
 * This class represents a configuration class that is used to configure the
 * security of the application.
 */
@Slf4j
@ConfigurationProperties(prefix = "nexus.security")
// @EnableMethodSecurity(prePostEnabled = true)
@Configuration
@RequiredArgsConstructor
@Import({ CausewayModuleExtSpringSecurityOAuth2.class })
@ConditionalOnProperty(prefix = "nexus.security.oauth2", name = "enabled", havingValue = "true")
public class CustomSecurityConfig {

    private final OAuth2ConfigProperties oauth2;
    private CorsConfig corsConfig = new CorsConfig();

    @Setter
    private String bearerTokenHeaderName = "x-forwarded-access-token";

    @Setter
    private String authoritiesClaimName = "roles";

    @Setter
    private boolean debug = false;

    @Setter
    private boolean useCsrf = false;

    @Setter
    private String authorityPrefix = "ROLE_";

    @Setter
    private PreAuthConfigProperties preauth = new PreAuthConfigProperties();

    @Bean
    public LoginForwardingController loginForwardingController() {
        return new LoginForwardingController();
    }

    @Bean
    public AppUserAutoCreationService appUserAutoCreationService(OAuth2ConfigProperties securityConfigProps,
            ApplicationUserRepository userRepo, ApplicationRoleRepository roleRepo, FactoryService factoryService) {
        return new AppUserAutoCreationService(securityConfigProps, userRepo, roleRepo, factoryService);
    }

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http)
            throws Exception {

        if (!useCsrf) {
            http.csrf(c -> c.disable());
        }
        http.cors(cors -> {
            cors.configurationSource(request -> {
                var c = new org.springframework.web.cors.CorsConfiguration();
                c.setAllowedOrigins(corsConfig.getAllowedHeaders());
                c.setAllowedMethods(corsConfig.getAllowedMethods());
                c.setAllowedHeaders(corsConfig.getAllowedHeaders());
                return c;
            });
        });

        log.info("**** Custom security enabled. creating clientFilterChain ****");

        applyPathAuthorization(http);

        applyOAuth2Login(http);

        applyResourceServer(http);

        if (preauth.isEnabled()) {
            log.info("adding preauth filter");
            // http.addFilter(new PreAuthFilter(preauth, authenticationManager));
        }

        var chain = http.build();
        log.info("security filter chain created: {}", chain);
        return chain;
    }

    private void applyOAuth2Login(HttpSecurity http) throws Exception {
        if (!oauth2.getLogin().isEnabled()) {
            return;
        }
        log.info("adding oauth2 login");
        http.oauth2Login(Customizer.withDefaults());
    }

    private void applyPathAuthorization(HttpSecurity http) throws Exception {
        var permitAllMatchers = new AntPathRequestMatcher[] {
                new AntPathRequestMatcher("/"),
                new AntPathRequestMatcher("/login/**"),
                new AntPathRequestMatcher("/logged-out"),
                new AntPathRequestMatcher("/error/**"),
                new AntPathRequestMatcher("/webjars/**"),
                new AntPathRequestMatcher("/oauth**"),
                new AntPathRequestMatcher("/api/public/**"),
                new AntPathRequestMatcher("/actuator/health"),
        };

        log.info("permitAllMatchers: {}", permitAllMatchers.length);
        for (AntPathRequestMatcher antPathRequestMatcher : permitAllMatchers) {
            log.info("permitAll matcher: {}", antPathRequestMatcher.getPattern());
        }

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(permitAllMatchers).permitAll()
                .anyRequest().authenticated());
    }

    private void applyResourceServer(HttpSecurity http) throws Exception {
        if (!oauth2.getResourceServer().isEnabled()) {
            log.info("resource server disabled");
            return;
        }

        log.info("Configuring OAuth2 resource server");
        http.oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(bearerTokenResolver())
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
    }

    private BearerTokenResolver bearerTokenResolver() {
        ProxyBearerTokenResolver bearerTokenResolver = new ProxyBearerTokenResolver();
        bearerTokenResolver.setBearerTokenHeaderName(bearerTokenHeaderName);
        // var bearerTokenResolver = new
        // HeaderBearerTokenResolver(bearerTokenHeaderName);
        return bearerTokenResolver;
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        // create a custom JWT converter to map the roles from the token as granted
        // authorities
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(authoritiesClaimName); // default is: scope, scp
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(authorityPrefix); // default is: SCOPE_

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

}