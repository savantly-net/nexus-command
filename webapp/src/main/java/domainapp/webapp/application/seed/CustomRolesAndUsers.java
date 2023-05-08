package domainapp.webapp.application.seed;

import java.util.function.Supplier;

import javax.inject.Inject;

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
import net.savantly.franchise.FranchiseModule;
import net.savantly.nexus.command.web.NexusCommandWebModule;

public class CustomRolesAndUsers extends FixtureScript {

    @Override
    protected void execute(ExecutionContext executionContext) {
        executionContext.executeChildren(this,
                new NexusCommandWebModuleSuperuserRole(),
                new FranchiseModuleSuperuserRole(),
                new ApplicationSuperuserRole(),
                new ApplicationUserRole(),
                new SvenUser(),
                new FranchiseeUser());
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
                    Can.of(ApplicationFeatureId.newNamespace(NexusCommandWebModule.NAMESPACE))
            );
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
                    Can.of(ApplicationFeatureId.newNamespace(FranchiseModule.NAMESPACE))
            );
        }
    }


    private static class ApplicationSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {

        public static final String ROLE_NAME = "application-superuser";

        public ApplicationSuperuserRole() {
            super(ROLE_NAME, "Permission to use everything in the 'application' module");
        }

        @Override
        protected void execute(ExecutionContext executionContext) {
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(ApplicationModule.PUBLIC_NAMESPACE))
            );
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
                    Can.of(ApplicationFeatureId.newNamespace(ApplicationModule.PUBLIC_NAMESPACE))
            );
        }
    }

    private static class SvenUser extends AbstractUserAndRolesFixtureScript {
        public SvenUser() {
            super(() -> "sven", () -> "pass", () -> AccountType.LOCAL, new RoleSupplier());
        }

        private static class RoleSupplier implements Supplier<Can<String>> {
            @Override
            public Can<String> get() {
                return Can.of(
                        causewayConfiguration.getExtensions().getSecman().getSeed().getRegularUser().getRoleName(), // built-in stuff
                        FranchiseModuleSuperuserRole.ROLE_NAME,
                        NexusCommandWebModuleSuperuserRole.ROLE_NAME,
                        ApplicationSuperuserRole.ROLE_NAME
                        );
            }
            @Inject CausewayConfiguration causewayConfiguration;
        }
    }


    private static class FranchiseeUser extends AbstractUserAndRolesFixtureScript {
        public FranchiseeUser() {
            super(() -> "franchisee", () -> "pass", () -> AccountType.LOCAL, new RoleSupplier());
        }

        private static class RoleSupplier implements Supplier<Can<String>> {
            @Override
            public Can<String> get() {
                return Can.of(
                        causewayConfiguration.getExtensions().getSecman().getSeed().getRegularUser().getRoleName(), // built-in stuff
                        ApplicationUserRole.ROLE_NAME
                        );
            }
            @Inject CausewayConfiguration causewayConfiguration;
        }
    }


}
