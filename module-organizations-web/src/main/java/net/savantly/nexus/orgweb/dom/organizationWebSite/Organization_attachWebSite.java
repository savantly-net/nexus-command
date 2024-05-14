package net.savantly.nexus.orgweb.dom.organizationWebSite;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;

import net.savantly.nexus.command.web.dom.site.WebSite;
import net.savantly.nexus.command.web.dom.site.WebSites;
import net.savantly.nexus.organizations.dom.organization.Organization;


@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Organization_attachWebSite {

    final Organization organization;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_attachWebSite>{}

    @Inject @Transient OrganizationWebSiteRepository repo;
    @Inject @Transient WebSites websites;
    


    @ActionLayout(named = "Attach Web Site", associateWith = "webSites", promptStyle = PromptStyle.DIALOG)
    public OrganizationWebSite act(
            final WebSite webSite) {
    	OrganizationWebSite organizationSite = OrganizationWebSite.withRequiredFields(webSite, organization);
        repo.save(organizationSite);
        return organizationSite;
    }

    @MemberSupport
    public Set<WebSite> choices0Act() {
        var sites = websites.listAll();
        var orgSites = repo.findByOrganizationIsNull().stream().map(OrganizationWebSite::getWebSite).collect(Collectors.toSet());
        return sites.stream()
        		.filter(s -> !orgSites.contains(s))
        		.collect(Collectors.toSet());
    }
}
