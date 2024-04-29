package net.savantly.nexus.projects.dom.personaGenerator;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import net.savantly.nexus.projects.dom.persona.PersonaDTO;

public interface PersonaGenerator {

    @SystemMessage("Generate a persona based on the provided context")
    @UserMessage("Context: {{context}}")
    PersonaDTO generatePersona(@V("context") String context);

}
