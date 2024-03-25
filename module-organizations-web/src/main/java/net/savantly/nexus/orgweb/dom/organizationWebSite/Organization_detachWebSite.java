package net.savantly.nexus.orgweb.dom.organizationWebSite;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;

import net.savantly.nexus.organizations.dom.organization.Organization;

@Action
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_detachWebSite {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_detachWebSite> {
    }

    @Inject
    @Transient
    OrganizationWebSiteRepository repo;

    @ActionLayout(named = "Detach Web Site", associateWith = "webSites", promptStyle = PromptStyle.DIALOG)
    public Organization act(
            @ParameterLayout(named = "Web Site") final OrganizationWebSite webSite) {
        repo.delete(webSite);
        return organization;
    }
    
    @MemberSupport
    public java.util.Collection<OrganizationWebSite> choices0Act() {
        return repo.findByOrganizationId(organization.getId());
    }
}
