package net.savantly.nexus.gaform;

import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.persistence.jpa.applib.CausewayModulePersistenceJpaApplib;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.causeway.testing.fixtures.applib.teardown.jpa.TeardownFixtureJpaAbstract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.savantly.nexus.gaform.dom.form.Form_SubmissionListener;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;

@Configuration
@Import({
        CausewayModulePersistenceJpaApplib.class,
})
@ComponentScan
@EnableJpaRepositories
@Slf4j
@EntityScan(basePackageClasses = { GoogleAnalyticsFlowModule.class })
@RequiredArgsConstructor
public class GoogleAnalyticsFlowModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.google.analytics.flow";
    public static final String SCHEMA = "google_analytics_flow";

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureJpaAbstract() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                // deleteFrom(FranchiseLocation.class);
            }
        };
    }

    @Bean
    public Form_SubmissionListener gaFormSubmissionListener(FactoryService factoryService,
            OrganizationSecrets secrets) {
        return new Form_SubmissionListener(factoryService, secrets);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

}