package net.savantly.nexus.agents.dom.organizationPersona;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_personas {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_personas> {
    }

    @Inject
    @Transient
    OrganizationPersonaRepository repository;

    @CollectionLayout(named = "Personas", describedAs = "Personas for this organization")
    public Set<OrganizationPersona> coll() {
        return repository.findByOrganizationId(organization.getId());
    }
}
