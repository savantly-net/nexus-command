package net.savantly.nexus.forms;

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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.savantly.ai.languagetools.LanguageToolModel;
import net.savantly.nexus.forms.dom.connections.datasource.DatasourceFactory;
import net.savantly.nexus.forms.dom.connections.jdbcConnection.JdbcConnections;
import net.savantly.nexus.forms.dom.connections.webhook.Webhooks;
import net.savantly.nexus.forms.dom.destinations.DestinationHookFactory;
import net.savantly.nexus.forms.dom.form.FormApi;
import net.savantly.nexus.forms.dom.generator.GeneralGenerator;

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
@EntityScan(basePackageClasses = { FormsModule.class })
@RequiredArgsConstructor
@DependsOn("aiConfig")
public class FormsModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.forms";
    public static final String SCHEMA = "forms";

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
    public GeneralGenerator forms_llmGeneralGenerator(LanguageToolModel model) {
        log.info("Creating issue generator with model: {}", model);
        return AiServices.builder(GeneralGenerator.class)
                .chatLanguageModel(model.asChatLanguageModel())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GeneralGenerator forms_defaultGeneralGenerator() {
        log.info("Creating default issue generator");
        return new GeneralGenerator() {
        };
    }

    @Bean
    public DestinationHookFactory forms_destinationHookFactory(ObjectMapper objectMapper, DatasourceFactory datasourceFactory, JdbcConnections jdbcConnections, Webhooks webhooks, RestTemplateBuilder restTemplateBuilder) {
        return new DestinationHookFactory(objectMapper, datasourceFactory, jdbcConnections, webhooks, restTemplateBuilder);
    }

    @Bean
    public DatasourceFactory forms_datasourceFactory() {
        return new DatasourceFactory();
    }

    @Bean
    @RequestMapping("${nexus.modules.forms.api-path:/api/forms}")
    public FormApi forms_formApi() {
        return new FormApi();
    }

}