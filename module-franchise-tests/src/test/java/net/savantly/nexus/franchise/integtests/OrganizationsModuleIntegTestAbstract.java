package net.savantly.nexus.franchise.integtests;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.causeway.core.config.presets.CausewayPresets;
import org.apache.causeway.core.runtimeservices.CausewayModuleCoreRuntimeServices;
import org.apache.causeway.extensions.secman.applib.role.dom.ApplicationRole;
import org.apache.causeway.extensions.secman.applib.tenancy.dom.ApplicationTenancy;
import org.apache.causeway.extensions.secman.applib.user.dom.AccountType;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUser;
import org.apache.causeway.extensions.secman.applib.user.dom.ApplicationUserRepository;
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

import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.OrganizationsModule;


@SpringBootTest(
        classes = OrganizationsModuleIntegTestAbstract.TestApp.class
)
@ActiveProfiles("test")
public abstract class OrganizationsModuleIntegTestAbstract extends CausewayIntegrationTestAbstractWithFixtures {

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
            OrganizationsModule.class,
            NexusCommandWebModule.class,
    })
    @PropertySources({
            @PropertySource(CausewayPresets.H2InMemory_withUniqueSchema),
            @PropertySource(CausewayPresets.UseLog4j2Test),
    })
    public static class TestApp {

        @Bean
        public ApplicationUserRepository applicationUserRepository() {
            return new ApplicationUserRepository() {
                @Override
                public List<ApplicationUser> allUsers() {
                    return Collections.emptyList();
                }

                @Override
                public ApplicationUser newApplicationUser() {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'newApplicationUser'");
                }

                @Override
                public Optional<ApplicationUser> findByUsername(String username) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
                }

                @Override
                public ApplicationUser findOrCreateUserByUsername(String username) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findOrCreateUserByUsername'");
                }

                @Override
                public Collection<ApplicationUser> find(String search) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'find'");
                }

                @Override
                public Collection<ApplicationUser> findByAtPath(String atPath) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findByAtPath'");
                }

                @Override
                public Collection<ApplicationUser> findByRole(ApplicationRole role) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findByRole'");
                }

                @Override
                public Collection<ApplicationUser> findByTenancy(ApplicationTenancy tenancy) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findByTenancy'");
                }

                @Override
                public Optional<ApplicationUser> findByEmailAddress(String emailAddress) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findByEmailAddress'");
                }

                @Override
                public Collection<ApplicationUser> findMatching(String search) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'findMatching'");
                }

                @Override
                public void enable(ApplicationUser user) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'enable'");
                }

                @Override
                public void disable(ApplicationUser user) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'disable'");
                }

                @Override
                public boolean isAdminUser(ApplicationUser user) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'isAdminUser'");
                }

                @Override
                public boolean isPasswordFeatureEnabled(ApplicationUser holder) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'isPasswordFeatureEnabled'");
                }

                @Override
                public boolean updatePassword(ApplicationUser user, String password) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
                }

                @Override
                public ApplicationUser newUser(String username, AccountType accountType,
                        Consumer<ApplicationUser> beforePersist) {
                    // TODO Auto-generated method stub
                    throw new UnsupportedOperationException("Unimplemented method 'newUser'");
                }
            };
        }
    }

}
