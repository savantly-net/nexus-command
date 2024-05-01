package net.savantly.nexus.projects;

import org.apache.causeway.extensions.fullcalendar.applib.CausewayModuleExtFullCalendarApplib;
import org.apache.causeway.extensions.pdfjs.applib.CausewayModuleExtPdfjsApplib;
import org.apache.causeway.persistence.jpa.applib.CausewayModulePersistenceJpaApplib;
import org.apache.causeway.testing.fakedata.applib.CausewayModuleTestingFakeDataApplib;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.causeway.testing.fixtures.applib.teardown.jpa.TeardownFixtureJpaAbstract;
import org.apache.causeway.valuetypes.markdown.applib.CausewayModuleValMarkdownApplib;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.savantly.ai.languagetools.LanguageToolModel;
import net.savantly.nexus.projects.dom.generator.GeneralGenerator;
import net.savantly.nexus.projects.dom.generator.IssueGenerator;
import net.savantly.nexus.projects.dom.generator.PersonaGenerator;

@Configuration
@Import({
        CausewayModuleExtPdfjsApplib.class,
        CausewayModuleExtFullCalendarApplib.class,
        CausewayModuleTestingFakeDataApplib.class,
        CausewayModulePersistenceJpaApplib.class,
        CausewayModuleValMarkdownApplib.class,
})
@ComponentScan
@EnableJpaRepositories
@Slf4j
@EntityScan(basePackageClasses = { ProjectsModule.class })
@RequiredArgsConstructor
@DependsOn("aiConfig")
public class ProjectsModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.projects";
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
    @Primary
    @ConditionalOnProperty("langchain4j.open-ai.chat-model.api-key")
    public PersonaGenerator personaGenerator(LanguageToolModel model) {
        log.info("Creating persona generator with model: {}", model);
        return AiServices.builder(PersonaGenerator.class)
                .chatLanguageModel(model.asChatLanguageModel())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public PersonaGenerator defaultPersonaGenerator() {
        log.info("Creating default persona generator");
        return new PersonaGenerator() {
        };
    }

    @Bean
    @Primary
    @ConditionalOnProperty("langchain4j.open-ai.chat-model.api-key")
    public GeneralGenerator llmGeneralGenerator(LanguageToolModel model) {
        log.info("Creating issue generator with model: {}", model);
        return AiServices.builder(GeneralGenerator.class)
                .chatLanguageModel(model.asChatLanguageModel())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GeneralGenerator defaultGeneralGenerator() {
        log.info("Creating default issue generator");
        return new GeneralGenerator() {
        };
    }

    @Bean
    @Primary
    @ConditionalOnProperty("langchain4j.open-ai.chat-model.api-key")
    public IssueGenerator llmIssueGenerator(LanguageToolModel model) {
        log.info("Creating issue generator with model: {}", model);
        return AiServices.builder(IssueGenerator.class)
                .chatLanguageModel(model.asChatLanguageModel())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public IssueGenerator defaultIssueGenerator() {
        log.info("Creating default issue generator");
        return new IssueGenerator() {
        };
    }
}