package net.savantly.nexus.projects.dom.organizationPersona;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import net.savantly.nexus.organizations.dom.organization.Organization;

@Collection
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_personas {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_personas> {
    }

    @Inject
    @Transient
    OrganizationPersonaRepository repository;

    @ActionLayout(named = "Personas", describedAs = "Personas for this organization")
    public Set<OrganizationPersona> coll() {
        return repository.findByOrganizationId(organization.getId());
    }
}
