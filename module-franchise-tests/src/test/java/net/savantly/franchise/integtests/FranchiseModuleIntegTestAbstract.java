package net.savantly.franchise.integtests;


import java.util.Collections;
import java.util.List;

import org.apache.causeway.core.config.presets.CausewayPresets;
import org.apache.causeway.core.runtimeservices.CausewayModuleCoreRuntimeServices;
import org.apache.causeway.persistence.jpa.eclipselink.CausewayModulePersistenceJpaEclipselink;
import org.apache.causeway.security.bypass.CausewayModuleSecurityBypass;
import org.apache.causeway.testing.fixtures.applib.CausewayIntegrationTestAbstractWithFixtures;
import org.apache.causeway.testing.fixtures.applib.CausewayModuleTestingFixturesApplib;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.ActiveProfiles;

import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.franchiseUser.FranchiseUser;
import net.savantly.franchise.dom.franchiseUser.FranchiseUsers;
import net.savantly.nexus.command.web.NexusCommandWebModule;


@SpringBootTest(
        classes = FranchiseModuleIntegTestAbstract.TestApp.class
)
@ActiveProfiles("test")
public abstract class FranchiseModuleIntegTestAbstract extends CausewayIntegrationTestAbstractWithFixtures {

    /**
     * Compared to the production app manifest <code>domainapp.webapp.AppManifest</code>,
     * here we in effect disable security checks, and we exclude any web/UI modules.
     */
    @SpringBootConfiguration
    @EnableAutoConfiguration
    @Import({

            CausewayModuleCoreRuntimeServices.class,
            CausewayModuleSecurityBypass.class,
            CausewayModulePersistenceJpaEclipselink.class,
            CausewayModuleTestingFixturesApplib.class,

            FranchiseModule.class,
            NexusCommandWebModule.class,
    })
    @PropertySources({
            @PropertySource(CausewayPresets.H2InMemory_withUniqueSchema),
            @PropertySource(CausewayPresets.UseLog4j2Test),
    })
    public static class TestApp {

        @Bean
        public FranchiseUsers franchiseUsers() {
            return new FranchiseUsers(){
                @Override
                public List<FranchiseUser> findAll() {
                    return Collections.emptyList();
                }
            };
        }
    }

}
