package net.savantly.nexus.flow.dom.flowContext;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecret;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;

@RequiredArgsConstructor
public class FlowContextFactory {

    private final OrganizationSecrets flowSecrets;

    public FlowContext create(String organizationId, Map<String, Object> variables) {
        var context = FlowContext.empty();
        if (Objects.nonNull(variables)) {
            context.setVariables(variables);
        }

        var secrets = flowSecrets.findByOrganizationId(organizationId);
        context.setSecrets(secrets.stream().collect(Collectors.toMap(OrganizationSecret::getName, OrganizationSecret::getSecret)));

        return context;
    }

    public FlowContext create(String organizationId) {
        return create(organizationId, null);
    }

}
