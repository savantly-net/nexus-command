package net.savantly.nexus.projects;

import org.apache.causeway.extensions.fullcalendar.applib.CausewayModuleExtFullCalendarApplib;
import org.apache.causeway.extensions.pdfjs.applib.CausewayModuleExtPdfjsApplib;
import org.apache.causeway.persistence.jpa.applib.CausewayModulePersistenceJpaApplib;
import org.apache.causeway.testing.fakedata.applib.CausewayModuleTestingFakeDataApplib;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.causeway.testing.fixtures.applib.teardown.jpa.TeardownFixtureJpaAbstract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import net.savantly.nexus.projects.dom.persona.Persona;
import net.savantly.nexus.projects.dom.personaGenerator.PersonaGenerator;

@Configuration
@Import({
        CausewayModuleExtPdfjsApplib.class,
        CausewayModuleExtFullCalendarApplib.class,
        CausewayModuleTestingFakeDataApplib.class,
        CausewayModulePersistenceJpaApplib.class,
})
@ComponentScan
@EnableJpaRepositories
@Slf4j
@EntityScan(basePackageClasses = { ProjectsModule.class })
public class ProjectsModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "projects";
    public static final String SCHEMA = "projects";

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureJpaAbstract() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                // deleteFrom(FranchiseLocation.class);
            }
        };
    }

    @Bean
    @ConditionalOnBean(ChatLanguageModel.class)
    public PersonaGenerator aiPersonaGenerator(ChatLanguageModel model) {
        log.info("Creating persona generator with model: {}", model);
        return AiServices.builder(PersonaGenerator.class)
                .chatLanguageModel(model)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(ChatLanguageModel.class)
    public PersonaGenerator defaultPersonaGenerator() {
        log.info("Creating default persona generator");
        return new PersonaGenerator() {
            @Override
            public Persona generatePersona(String context) {
                return Persona.withName("generated-persona");
            }
        };
    }
}