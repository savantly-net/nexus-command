package net.savantly.nexus.projects.dom.generator;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import net.savantly.nexus.projects.dom.persona.PersonaDTO;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaDetailsDTO;

public interface PersonaGenerator {

    @SystemMessage("Generate a persona based on the provided context. The first name should start with the letter {{firstNameLetter}}.")
    @UserMessage("Context: {{context}}")
    default PersonaDTO generatePersona(@V("context") String context, @V("firstNameLetter") String firstNameLetter){
        return new PersonaDTO().setName("Generated Persona").setDescription(context);
    }

    @SystemMessage("Generate details for the persona based on the project information")
    @UserMessage("--CONTEXT--\n{{projectContext}}\n\nPersona: {{persona}}\n--END CONTEXT--")
    default ProjectPersonaDetailsDTO generateProjectPersonaDetails(@V("projectContext") String projectContext, @V("persona") String personaContext) {
        return new ProjectPersonaDetailsDTO();
    }

}
