package net.savantly.nexus.projects.dom.organizationPersona;

import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.commons.lang3.RandomStringUtils;

import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.projects.dom.generator.PersonaGenerator;
import net.savantly.nexus.projects.dom.persona.Persona;
import net.savantly.nexus.projects.dom.persona.PersonaDTO;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_createPersona {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_createPersona> {
    }

    @Inject
    @Transient
    PersonaGenerator personaGenerator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Create Persona", associateWith = "personas", promptStyle = PromptStyle.DIALOG, position = ActionLayout.Position.PANEL)
    public Persona act(
            @ParameterLayout(named = "Description") final String description) {

        final String nameFirstLetter = RandomStringUtils.randomAlphabetic(1).toUpperCase();
        final PersonaDTO personaDto = personaGenerator.generatePersona(formatContext(description), nameFirstLetter);
        personaDto.setId(UUID.randomUUID().toString());
        final Persona persona = Persona.fromDto(personaDto);
        OrganizationPersona organizationPersona = OrganizationPersona.withRequiredFields(persona, organization);
        repositoryService.persist(organizationPersona);
        return persona;
    }

    private String formatContext(String description) {
        return "Organization: " + organization.getName() + ", Description: " + description;
    }
}
