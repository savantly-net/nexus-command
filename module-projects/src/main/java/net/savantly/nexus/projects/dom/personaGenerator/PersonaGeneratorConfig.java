package net.savantly.nexus.projects.dom.personaGenerator;

import javax.inject.Inject;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import net.savantly.nexus.projects.dom.persona.Persona;

@Configuration
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PersonaGeneratorConfig {

    @Bean
    @ConditionalOnBean(ChatLanguageModel.class)
    public PersonaGenerator personaGenerator(ChatLanguageModel model) {
        return AiServices.builder(PersonaGenerator.class)
                .chatLanguageModel(model)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(ChatLanguageModel.class)
    public PersonaGenerator personaGenerator() {
        return new PersonaGenerator() {
            @Override
            public Persona generatePersona(String context) {
                return Persona.withName("generated-persona");
            }
        };
    }
}
