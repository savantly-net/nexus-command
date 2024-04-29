package net.savantly.nexus.projects.dom.organizationPersona;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.projects.dom.persona.Persona;

@Action
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_createPersona {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_createPersona> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Create Persona", associateWith = "personas", promptStyle = PromptStyle.DIALOG)
    public OrganizationPersona act(
            @ParameterLayout(named = "Persona Name") final String name) {
        final Persona persona = Persona.withName(name);
        OrganizationPersona organizationPersona = OrganizationPersona.withRequiredFields(persona, organization);
        repositoryService.persist(organizationPersona);
        return organizationPersona;
    }
}
