package net.savantly.nexus.orgweb.dom.organizationWebSite;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.nexus.command.web.dom.site.WebSite;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Organization_createWebSite {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_createWebSite> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Create Web Site", associateWith = "webSites", promptStyle = PromptStyle.DIALOG)
    public OrganizationWebSite act(
            @ParameterLayout(named = "Web Site Name") final String name) {
        final WebSite webSite = WebSite.withRequiredFields(name);
        OrganizationWebSite organizationSite = OrganizationWebSite.withRequiredFields(webSite, organization);
        repositoryService.persist(organizationSite);
        return organizationSite;
    }
}
