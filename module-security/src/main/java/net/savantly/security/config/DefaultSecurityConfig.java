package net.savantly.security.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ConfigurationProperties(prefix = "nexus.security")
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "nexus.security.oauth2", name = "enabled", havingValue = "false", matchIfMissing = true)
public class DefaultSecurityConfig {

    private boolean debug = false;
    private boolean useCsrf = false;
    private final CorsConfig corsConfig;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> {
            web.debug(debug);
        };
    }

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http)
            throws Exception {

        if (!useCsrf) {
            http.csrf(c -> c.disable());
        }
        log.info("**** Using default security chain ****");
        http.authorizeHttpRequests(authz -> authz.requestMatchers("/**").permitAll());
        http.formLogin(Customizer.withDefaults());
        http.cors(cors -> {
            cors.configurationSource(request -> {
                var c = new org.springframework.web.cors.CorsConfiguration();
                c.setAllowedOrigins(corsConfig.getAllowedHeaders());
                c.setAllowedMethods(corsConfig.getAllowedMethods());
                c.setAllowedHeaders(corsConfig.getAllowedHeaders());
                return c;
            });
        });
        return http.build();
    }
}
