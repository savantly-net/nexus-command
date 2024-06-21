package domainapp.webapp.application.seed.modules;

import java.util.List;

import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionMode;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionRule;
import org.apache.causeway.extensions.secman.applib.role.fixtures.AbstractRoleAndPermissionsFixtureScript;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class BaseModuleRoleFixtureScript {


    private final String moduleName;
    private final String namespace;

    @Getter
    private AbstractRoleAndPermissionsFixtureScript adminRoleFixtureScript;
    @Getter
    private AbstractRoleAndPermissionsFixtureScript userRoleFixtureScript;


    public BaseModuleRoleFixtureScript(String moduleName, String namespace) {
        this.moduleName = moduleName;
        this.namespace = namespace;

        var superUserRoleName = String.format("%s-superuser", moduleName);
        var userRoleName = String.format("%s-viewer", moduleName);

        this.adminRoleFixtureScript = new ModuleSuperuserRole(superUserRoleName);
        this.userRoleFixtureScript = new ModuleUserRole(userRoleName);
    }

    private class ModuleSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {
        public ModuleSuperuserRole(final String roleName) {
            super(roleName, String.format("Permission to use everything in the '%s' module", moduleName));
        }
        @Override
        protected void execute(ExecutionContext executionContext) {
            log.info("Creating role: {}", this.getRoleName());
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.CHANGING,
                    Can.of(ApplicationFeatureId.newNamespace(namespace)));
        }
    }

    private class ModuleUserRole extends AbstractRoleAndPermissionsFixtureScript {
        public ModuleUserRole(final String roleName) {
            super(roleName, String.format("Permission to view everything in the '%s' module", moduleName));
        }
        @Override
        protected void execute(ExecutionContext executionContext) {
            log.info("Creating role: {}", this.getRoleName());
            newPermissions(
                    ApplicationPermissionRule.ALLOW,
                    ApplicationPermissionMode.VIEWING,
                    Can.of(ApplicationFeatureId.newNamespace(namespace)));
        }
    }

    public List<AbstractRoleAndPermissionsFixtureScript> getRoles() {
        return List.of(adminRoleFixtureScript, userRoleFixtureScript);
    }
}