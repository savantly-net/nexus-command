package net.savantly.nexus.projects.dom.generator;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import net.savantly.nexus.projects.dom.persona.PersonaDTO;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaDetailsDTO;

public interface PersonaGenerator {

    @SystemMessage("Generate a persona based on the provided context")
    @UserMessage("Context: {{context}}")
    default PersonaDTO generatePersona(@V("context") String context) {
        return new PersonaDTO().setName("Generated Persona").setDescription(context);
    }

    @SystemMessage("Generate details for the persona based on the project information")
    @UserMessage("Project: {{projectContext}}")
    default ProjectPersonaDetailsDTO generateProjectPersonaDetails(@V("projectContext") String projectContext) {
        return new ProjectPersonaDetailsDTO();
    }

}
