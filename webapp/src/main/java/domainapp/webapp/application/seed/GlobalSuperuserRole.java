package domainapp.webapp.application.seed;

import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;
import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionMode;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionRule;
import org.apache.causeway.extensions.secman.applib.role.fixtures.AbstractRoleAndPermissionsFixtureScript;

public class GlobalSuperuserRole extends AbstractRoleAndPermissionsFixtureScript {

    public static final String ROLE_NAME = "global-superuser";

    public GlobalSuperuserRole() {
        super(ROLE_NAME, "Permission to administer all modules");
    }

    @Override
    protected void execute(ExecutionContext executionContext) {
        newPermissions(
                ApplicationPermissionRule.ALLOW,
                ApplicationPermissionMode.CHANGING,
                Can.of(ApplicationFeatureId.newNamespace("*")));
    }
}