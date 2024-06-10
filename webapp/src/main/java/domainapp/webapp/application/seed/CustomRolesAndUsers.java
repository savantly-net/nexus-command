package domainapp.webapp.application.seed;

import java.util.function.Supplier;

import jakarta.inject.Inject;

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
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.orgweb.OrgWebModule;
import net.savantly.nexus.products.ProductsModule;
import net.savantly.nexus.projects.ProjectsModule;

public class CustomRolesAndUsers extends FixtureScript {

    private final NexusAppProperties nexusAppProperties;

    public CustomRolesAndUsers(NexusAppProperties nexusAppProperties) {
        this.nexusAppProperties = nexusAppProperties;
    }

    @Override
    protected void execute(ExecutionContext executionContext) {
        executionContext.executeChildren(this,
                new OrganizationsModuleSuperuserRole(),
                new OrgWebModuleSuperuserRole(),
                new ProductsModuleSuperuserRole(),
                new ProjectsModuleSuperuserRole(),
                new FlowModuleSuperuserRole(),
                new NexusCommandWebModuleSuperuserRole(),
                new FranchiseModuleSuperuserRole(),
                new ApplicationSuperuserRole(),
                new ApplicationUserRole());
        if (nexusAppProperties.getAdminSeed().isEnabled()) {
            executionContext.executeChildren(this, new AdminUser(nexusAppProperties.getAdminSeed().getUsername(),
                    nexusAppProperties.getAdminSeed().getPassword()));
        }
        if (nexusAppProperties.getUserSeed().isEnabled()) {
            executionContext.executeChildren(this, new UnprivilegedUser(nexusAppProperties.getUserSeed().getUsername(),
                    nexusAppProperties.getUserSeed().getPassword()));
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

        public static final String ROLE_NAME = "forms-superuser";

        public FlowModuleSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'Forms' module");
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
                        ApplicationSuperuserRole.ROLE_NAME);
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
