package net.savantly.security.config;

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
@ConditionalOnProperty(prefix = "nexus.security", name = "enabled", havingValue = "false", matchIfMissing = true)
public class DefaultSecurityConfig {

    private boolean debug = false;
    private boolean useCsrf = false;

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
        return http.build();
    }
}
