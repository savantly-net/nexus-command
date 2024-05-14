package net.savantly.nexus.audited.config;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.savantly.security.users.UserProvider;

/**
 * Configuration for the AuditedEntityModule
 */
@Slf4j
@Configuration("auditedConfig")
@ConfigurationProperties(prefix = "nexus.modules.audited")
@Data
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditedEntityConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        log.info("AuditedEntityConfig initialized");

    }

    @Bean
    public AuditorAware<String> auditorAware(UserProvider userProvider) {
        return () -> Optional.of(userProvider.getCurrentUser());
    }

}