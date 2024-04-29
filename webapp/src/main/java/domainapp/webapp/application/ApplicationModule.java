package domainapp.webapp.application;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import net.savantly.nexus.command.franchise.organizations.FranchiseOrganizationsModule;
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.api.WebApiModule;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.orgweb.OrgWebModule;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.security.SecurityModule;
import net.savantly.ai.AIModule;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan
public class ApplicationModule {

    public static final String PUBLIC_NAMESPACE = "public";

    @ConditionalOnProperty(value = "nexus.modules.security.enabled", havingValue = "true")
    @Import(SecurityModule.class)
    static class SecurityModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.modules.organizations.enabled", havingValue = "true")
    @Import(OrganizationsModule.class)
    static class OrganizationsModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.modules.ai.enabled", havingValue = "true")
    @Import(AIModule.class)
    static class AIModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.modules.projects.enabled", havingValue = "true")
    @Import(ProjectsModule.class)
    static class ProjectsModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.modules.franchise.enabled", havingValue = "true")
    @Import({ FranchiseModule.class, FranchiseOrganizationsModule.class })
    static class FranchiseModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.modules.web.enabled", havingValue = "true")
    @Import({ NexusCommandWebModule.class, WebApiModule.class })
    static class NexusCommandWebModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.modules.org-web.enabled", havingValue = "true")
    @Import(OrgWebModule.class)
    static class OrgWebModuleConfigurer {
    }

}
