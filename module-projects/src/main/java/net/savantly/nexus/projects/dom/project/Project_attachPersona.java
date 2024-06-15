package net.savantly.nexus.projects.dom.project;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import lombok.extern.log4j.Log4j2;
import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.agents.dom.generator.PersonaGenerator;
import net.savantly.nexus.agents.dom.persona.Persona;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersona;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaDetailsDTO;

@Log4j2
@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Project_attachPersona {

    final Project project;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_attachPersona> {
    }

    @Inject
    @Transient
    PersonaGenerator personaGenerator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Attach Persona", associateWith = "personas", promptStyle = PromptStyle.DIALOG)
    public ProjectPersona act(
            @ParameterLayout(named = "Persona") final Persona persona) {

        var projectContext = project.getPrompt();
        var personaContext = formatContext(persona);

        //log.debug("Generating ProjectPersona details using the contexts: {} {}", projectContext, personaContext);

        // final ProjectPersonaDetailsDTO personaDetailsDto = personaGenerator
        //         .generateProjectPersonaDetails(projectContext, personaContext);

        ProjectPersona projectPersona = ProjectPersona.withRequiredFields(persona, project);
        projectPersona.setDetailsFromDto(new ProjectPersonaDetailsDTO());
        repositoryService.persist(projectPersona);
        return projectPersona;
    }

    private String formatContext(Persona persona) {
        var personaPrompt = PromptBuilder.format(persona);
        var sb = new StringBuilder();
        sb.append(personaPrompt);
        return sb.toString();
    }
}
