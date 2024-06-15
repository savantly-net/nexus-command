package net.savantly.nexus.agents.dom.generator;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import net.savantly.nexus.agents.dom.persona.PersonaDTO;

public interface PersonaGenerator {

    @SystemMessage("Generate a persona based on the provided context. The first name should start with the letter {{firstNameLetter}}.")
    @UserMessage("Context: {{context}}")
    default PersonaDTO generatePersona(@V("context") String context, @V("firstNameLetter") String firstNameLetter){
        return new PersonaDTO().setName("Generated Persona").setDescription(context);
    }


}
