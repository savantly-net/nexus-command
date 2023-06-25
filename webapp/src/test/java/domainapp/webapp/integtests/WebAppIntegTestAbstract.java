package domainapp.webapp.integtests;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.apache.causeway.core.config.presets.CausewayPresets;
import org.apache.causeway.core.runtimeservices.CausewayModuleCoreRuntimeServices;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
import org.apache.causeway.persistence.jpa.eclipselink.CausewayModulePersistenceJpaEclipselink;
import org.apache.causeway.security.bypass.CausewayModuleSecurityBypass;
import org.apache.causeway.testing.fixtures.applib.CausewayModuleTestingFixturesApplib;
import org.apache.causeway.testing.integtestsupport.applib.CausewayIntegrationTestAbstract;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.ActiveProfiles;

import domainapp.webapp.application.ApplicationModule;
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.franchise.FranchiseModule;

@SpringBootTest(
    classes = {
            // we use a slightly different configuration compared to the production (AppManifest/webapp)
            WebAppIntegTestAbstract.TestApp.class,
            ApplicationModule.class,
    }
)
@ActiveProfiles("test")
public abstract class WebAppIntegTestAbstract extends CausewayIntegrationTestAbstract {

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

        NexusCommandWebModule.class,
        FranchiseModule.class
    })
    @PropertySources({
        @PropertySource(CausewayPresets.H2InMemory_withUniqueSchema),
        @PropertySource(CausewayPresets.UseLog4j2Test),
    })
    public static class TestApp {


        @Bean
        public ApplicationUserRepository applicationUserRepository() {
            var mock = Mockito.mock(ApplicationUserRepository.class);
            when(mock.allUsers()).thenReturn(
                    Collections.emptyList());
            return mock;
        }

    }
}
