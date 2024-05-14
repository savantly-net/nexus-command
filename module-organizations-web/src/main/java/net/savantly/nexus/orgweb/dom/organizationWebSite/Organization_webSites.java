package net.savantly.nexus.orgweb.dom.organizationWebSite;

import java.util.Set;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import net.savantly.nexus.organizations.dom.organization.Organization;


@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Organization_webSites {

    final Organization organization;

    public static class ActionEvent extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_webSites>{}

    @Inject @Transient OrganizationWebSiteRepository repository;

    @ActionLayout(named = "Web Sites", describedAs = "Web Sites for this organization")
    public Set<OrganizationWebSite> coll() {
        return repository.findByOrganizationId(organization.getId());
    }
}
