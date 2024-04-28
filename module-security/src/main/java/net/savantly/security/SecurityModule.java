package net.savantly.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.savantly.security.config.SecurityConfig;

@Configuration
@Import(SecurityConfig.class)
public class SecurityModule {
    
}
