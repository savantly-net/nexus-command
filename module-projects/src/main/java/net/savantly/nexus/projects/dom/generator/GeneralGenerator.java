package net.savantly.nexus.projects.dom.generator;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public interface GeneralGenerator {

    @SystemMessage("{{system}}")
    @UserMessage("Context: {{context}}")
    default String generateText(@V("system") String system, @V("context") String context) {
        return "generator offline";
    }
}
