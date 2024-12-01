package domainapp.webapp.application.seed;

import java.util.function.Supplier;

import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.core.config.CausewayConfiguration;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionMode;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionRule;
import org.apache.causeway.extensions.secman.applib.role.fixtures.AbstractRoleAndPermissionsFixtureScript;
import org.apache.causeway.extensions.secman.applib.user.dom.AccountType;
import org.apache.causeway.extensions.secman.applib.user.fixtures.AbstractUserAndRolesFixtureScript;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;

import domainapp.webapp.application.ApplicationModule;
import domainapp.webapp.properties.NexusAppProperties;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.agents.AgentsModule;
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.orgweb.OrgWebModule;
import net.savantly.nexus.products.ProductsModule;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.webhooks.WebhooksModule;

@Log4j2
public class CustomRolesAndUsers extends FixtureScript {

    private final NexusAppProperties nexusAppProperties;

    public CustomRolesAndUsers(NexusAppProperties nexusAppProperties) {
        this.nexusAppProperties = nexusAppProperties;
    }

    @Override
    protected void execute(ExecutionContext executionContext) {
        log.info("Creating custom roles and users");
        executionContext.executeChildren(this,
                new OrganizationsModuleSuperuserRole(),
                new OrgWebModuleSuperuserRole(),
                new ProductsModuleSuperuserRole(),
                new ProjectsModuleSuperuserRole(),
                new FlowModuleSuperuserRole(),
                new NexusCommandWebModuleSuperuserRole(),
                new FranchiseModuleSuperuserRole(),
                new ApplicationSuperuserRole(),
                new ApplicationUserRole(),
                new AgentsModuleSuperuserRole(),
                new WebhooksModuleSuperuserRole(),
                new KafkaModuleSuperuserRole(),
                new KafkaModuleUserRole());
        if (nexusAppProperties.getSeed().getAdmin().isEnabled()) {
            log.info("Creating admin user: {}", nexusAppProperties.getSeed().getAdmin().getUsername());
            executionContext.executeChildren(this, new AdminUser(nexusAppProperties.getSeed().getAdmin().getUsername(),
                    nexusAppProperties.getSeed().getAdmin().getPassword()));
        }
        if (nexusAppProperties.getSeed().getUser().isEnabled()) {
            log.info("Creating unprivileged user: {}", nexusAppProperties.getSeed().getUser().getUsername());
            executionContext.executeChildren(this,
                    new UnprivilegedUser(nexusAppProperties.getSeed().getUser().getUsername(),
                            nexusAppProperties.getSeed().getUser().getPassword()));
        }
    }

    private static class OrganizationsModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "organizations-superuser";

        public OrganizationsModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Organizations' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(OrganizationsModule.NAMESPACE)));
        }
    }

    private static class OrgWebModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "organizations-web-superuser";

        public OrgWebModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'OrgWeb' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(OrgWebModule.NAMESPACE)));
        }
    }

    private static class ProductsModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "products-superuser";

        public ProductsModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Products' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(ProductsModule.NAMESPACE)));
        }
    }

    private static class AgentsModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "agents-superuser";

        public AgentsModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Agents' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(AgentsModule.NAMESPACE)));
        }
    }

    private static class ProjectsModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "projects-superuser";

        public ProjectsModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Projects' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(ProjectsModule.NAMESPACE)));
        }
    }

    private static class FlowModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "flow-superuser";

        public FlowModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Flow' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(FlowModule.NAMESPACE)));
        }
    }

    private static class NexusCommandWebModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "nexus-web-superuser";

        public NexusCommandWebModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Nexus Command Web' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(NexusCommandWebModule.NAMESPACE)));
        }
    }

    private static class FranchiseModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {

        public static final String ROLE_NAME = "franchise-superuser";

        public FranchiseModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'franchise' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(FranchiseModule.NAMESPACE)));
        }
    }

    private static class WebhooksModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "webhooks-superuser";

        public WebhooksModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'webhooks' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(WebhooksModule.NAMESPACE)));
        }
    }

    private static class KafkaModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "kafka-superuser";

        public KafkaModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'kafka' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace("nexus.kafka")));
        }
    }

    private static class KafkaModuleUserRole extends AbstractRoleAndPermissionsFixtureScript {
        public static final String ROLE_NAME = "kafka-user";

        public KafkaModuleUserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'kafka' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.VIEWING,
                    Can.of(ApplicationFeatureId.newNamespace("nexus.kafka")));
        }
    }

    private static class ApplicationSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {

        public static final String ROLE_NAME = "application-superuser";

        public ApplicationSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the application");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace("*")));
        }
    }

    private static class ApplicationUserRole extends AbstractRoleAndPermissionsFixtureScript {

        public static final String ROLE_NAME = "application-user";

        public ApplicationUserRole() {
            super(ROLE_NAME, "Permission to read everything in the 'application' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.VIEWING,
                    Can.of(ApplicationFeatureId.newNamespace(ApplicationModule.PUBLIC_NAMESPACE)));
        }
    }

    /**************/
    /*******
     * USERS
     * /
     **************/

    private static class AdminUser extends AbstractUserAndRolesFixtureScript {
        public AdminUser(String username, String password) {
            super(() -> username, () -> password, () -> AccountType.LOCAL, new RoleSupplier());
        }

        private static class RoleSupplier implements Supplier<Can<String>> {
            @Override
            public Can<String> get() {
                return Can.of(
                        causewayConfiguration.getExtensions().getSecman().getSeed().getAdmin().getRoleName(), // built-in
                                                                                                              // stuff
                        OrgWebModuleSuperuserRole.ROLE_NAME,
                        OrganizationsModuleSuperuserRole.ROLE_NAME,
                        ProductsModuleSuperuserRole.ROLE_NAME,
                        ProjectsModuleSuperuserRole.ROLE_NAME,
                        FlowModuleSuperuserRole.ROLE_NAME,
                        FranchiseModuleSuperuserRole.ROLE_NAME,
                        NexusCommandWebModuleSuperuserRole.ROLE_NAME,
                        ApplicationSuperuserRole.ROLE_NAME,
                        AgentsModuleSuperuserRole.ROLE_NAME,
                        WebhooksModuleSuperuserRole.ROLE_NAME,
                        KafkaModuleSuperuserRole.ROLE_NAME);
            }

            @Inject
            CausewayConfiguration causewayConfiguration;
        }
    }

    private static class UnprivilegedUser extends AbstractUserAndRolesFixtureScript {
        public UnprivilegedUser(String username, String password) {
            super(() -> username, () -> password, () -> AccountType.LOCAL, new RoleSupplier());
        }

        private static class RoleSupplier implements Supplier<Can<String>> {
            @Override
            public Can<String> get() {
                return Can.of(
                        causewayConfiguration.getExtensions().getSecman().getSeed().getRegularUser().getRoleName(), // built-in
                                                                                                                    // stuff
                        ApplicationUserRole.ROLE_NAME);
            }

            @Inject
            CausewayConfiguration causewayConfiguration;
        }
    }

}
