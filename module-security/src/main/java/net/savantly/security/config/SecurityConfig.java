package net.savantly.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Import({ DefaultSecurityConfig.class, CustomSecurityConfig.class })
public class SecurityConfig {

}