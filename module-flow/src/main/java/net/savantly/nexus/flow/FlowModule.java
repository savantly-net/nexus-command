package net.savantly.nexus.flow;

import org.apache.causeway.applib.services.repository.RepositoryService;
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

import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.savantly.ai.languagetools.LanguageToolModel;
import net.savantly.nexus.flow.api.FlowService;
import net.savantly.nexus.flow.dom.connections.datasource.DatasourceFactory;
import net.savantly.nexus.flow.dom.connections.flowHook.FlowDestinationHookFactory;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnections;
import net.savantly.nexus.flow.dom.connections.webhook.Webhooks;
import net.savantly.nexus.flow.dom.destinations.DestinationHookFactory;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitionExecutionProxy;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitionRepository;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDiscoveryService;
import net.savantly.nexus.flow.dom.flowSecret.FlowSecretRepository;
import net.savantly.nexus.flow.dom.generator.GeneralGenerator;
import net.savantly.nexus.flow.executor.FlowExecutorFactory;
import net.savantly.nexus.flow.executor.FlowNodeFactory;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

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
@EntityScan(basePackageClasses = { FlowModule.class })
@RequiredArgsConstructor
@DependsOn("aiConfig")
public class FlowModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.flow";
    public static final String SCHEMA = "flow";

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
    public GeneralGenerator flow_llmGeneralGenerator(LanguageToolModel model) {
        log.info("Creating issue generator with model: {}", model);
        return AiServices.builder(GeneralGenerator.class)
                .chatLanguageModel(model.asChatLanguageModel())
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public GeneralGenerator flow_defaultGeneralGenerator() {
        log.info("Creating default issue generator");
        return new GeneralGenerator() {
        };
    }

    @Bean
    public FlowDestinationHookFactory flow_flowDestinationHookFactory(FlowDefinitions flowDefinitions,
            FlowDefinitionExecutionProxy flowDefinitionExecutionProxy) {
        return new FlowDestinationHookFactory(flowDefinitions, flowDefinitionExecutionProxy);
    }

    @Bean
    public DestinationHookFactory flow_destinationHookFactory(ObjectMapper objectMapper,
            DatasourceFactory datasourceFactory, JdbcConnections jdbcConnections, Webhooks webhooks,
            RestTemplateBuilder restTemplateBuilder, FlowDestinationHookFactory flowDestinationHookFactory) {
        return new DestinationHookFactory(objectMapper, datasourceFactory, jdbcConnections, webhooks,
                restTemplateBuilder, flowDestinationHookFactory);
    }

    @Bean
    public DatasourceFactory flow_datasourceFactory() {
        return new DatasourceFactory();
    }

    @Bean
    public JavascriptExecutor flow_javascriptExecutor(IJavetEnginePool javetEnginePool) {
        return new JavascriptExecutor(javetEnginePool);
    }

    @Bean
    public FlowNodeDiscoveryService flowNodeDiscoveryService() {
        return new FlowNodeDiscoveryService();
    }

    @Bean
    public FlowNodeFactory flowNodeFactory(JavascriptExecutor javascriptExecutor) {
        return new FlowNodeFactory(javascriptExecutor);
    }

    @Bean
    public FlowExecutorFactory flowExecutorFactory(FlowNodeFactory nodeFactory) {
        return new FlowExecutorFactory(nodeFactory);
    }

    @Bean
    public FlowDefinitionExecutionProxy flowDefinitionExecutionProxy(FlowExecutorFactory flowExecutorFactory,
            FlowDefinitionRepository flowDefinitionRepository, ObjectMapper objectMapper,
            RepositoryService repositoryService, FlowSecretRepository secretRepository) {
        return new FlowDefinitionExecutionProxy(flowExecutorFactory, flowDefinitionRepository, objectMapper,
                repositoryService, secretRepository);
    }

    @Bean
    public FlowService flowService(FlowDefinitions flowDefinitions, FlowNodeDiscoveryService flowNodeDiscoveryService) {
        return new FlowService(flowDefinitions, flowNodeDiscoveryService);
    }

}