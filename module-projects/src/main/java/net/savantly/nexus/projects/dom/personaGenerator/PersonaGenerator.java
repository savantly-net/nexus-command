package net.savantly.nexus.projects.dom.personaGenerator;

import net.savantly.nexus.projects.dom.persona.Persona;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface PersonaGenerator {
    
    @SystemMessage("Generate a persona based on the provided context")
    @UserMessage("Context: {{context}}")
    Persona generatePersona(@V("context") String context);
    
}
