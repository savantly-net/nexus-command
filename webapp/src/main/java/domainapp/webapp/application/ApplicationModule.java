package domainapp.webapp.application;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import domainapp.webapp.properties.NexusAppProperties;
import lombok.RequiredArgsConstructor;
import net.savantly.ai.AIModule;
import net.savantly.encryption.jpa.AttributeEncryptor;
import net.savantly.nexus.agents.AgentsModule;
import net.savantly.nexus.audited.AuditedEntityModule;
import net.savantly.nexus.command.franchise.organizations.FranchiseOrganizationsModule;
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.api.WebApiModule;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.orgfees.OrganizationFeesModule;
import net.savantly.nexus.orgweb.OrgWebModule;
import net.savantly.nexus.products.ProductsModule;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.webhooks.WebhooksModule;
import net.savantly.security.SecurityModule;

@Configuration
@ComponentScan
@EnableJpaRepositories
@EntityScan
@RequiredArgsConstructor
public class ApplicationModule {

    public static final String PUBLIC_NAMESPACE = "public";

    private final NexusAppProperties properties;


    @Import(SecurityModule.class)
    static class SecurityModuleConfigurer {
    }

    @Bean
    @ConditionalOnMissingBean
    public AttributeEncryptor attributeEncryptor() throws Exception {
        return new AttributeEncryptor(properties.getEncryption().getSecret());
    }

    @ConditionalOnProperty(value = "nexus.audited-entity.enabled", havingValue = "true")
    @Import(AuditedEntityModule.class)
    static class AuditedEntityModuleConfigurer {
    }


    @ConditionalOnProperty(value = "nexus.organizations.enabled", havingValue = "true")
    @Import(OrganizationsModule.class)
    static class OrganizationsModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.ai.enabled", havingValue = "true")
    @Import(AIModule.class)
    static class AIModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.agents.enabled", havingValue = "true")
    @Import(AgentsModule.class)
    static class AgentsModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.projects.enabled", havingValue = "true")
    @Import(ProjectsModule.class)
    static class ProjectsModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.flow.enabled", havingValue = "true")
    @Import(FlowModule.class)
    static class FlowModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.webhooks.enabled", havingValue = "true")
    @Import({ WebhooksModule.class })
    static class WebHooksModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.franchise.enabled", havingValue = "true")
    @Import({ FranchiseModule.class })
    static class FranchiseModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.web.enabled", havingValue = "true")
    @Import({ NexusCommandWebModule.class, WebApiModule.class })
    static class NexusCommandWebModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.products.enabled", havingValue = "true")
    @Import({ ProductsModule.class })
    static class NexusCommandProductsModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.org-web.enabled", havingValue = "true")
    @Import(OrgWebModule.class)
    static class OrgWebModuleConfigurer {
    }

    @ConditionalOnProperty(value = "nexus.org-fees.enabled", havingValue = "true")
    @Import(OrganizationFeesModule.class)
    static class OrgFeesModuleConfigurer {
    }

}
