package net.savantly.nexus.flow;

import org.apache.causeway.applib.services.email.EmailService;
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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.caoccao.javet.interop.engine.IJavetEnginePool;
import com.fasterxml.jackson.databind.ObjectMapper;

import dev.langchain4j.service.AiServices;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.savantly.ai.languagetools.LanguageToolModel;
import net.savantly.nexus.flow.api.FlowService;
import net.savantly.nexus.flow.dom.connections.datasource.DatasourceFactory;
import net.savantly.nexus.flow.dom.connections.flowHook.FlowDestinationHookFactory;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnections;
import net.savantly.nexus.flow.dom.connections.kafka.KafkaDestinationHookFactory;
import net.savantly.nexus.flow.dom.connections.webhook.WebhookDestinationHookFactory;
import net.savantly.nexus.flow.dom.destination.DestinationHookFactory;
import net.savantly.nexus.flow.dom.emailTarget.EmailDestinationHookFactory;
import net.savantly.nexus.flow.dom.emailTarget.EmailTargets;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitionExecutionProxy;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitionRepository;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinitions;
import net.savantly.nexus.flow.dom.flowNode.FlowNodeDiscoveryService;
import net.savantly.nexus.flow.dom.flowNodeSchema.FlowNodeSchemaGenerator;
import net.savantly.nexus.flow.dom.form.FormRepository;
import net.savantly.nexus.flow.dom.form.FormSubmissionProxy;
import net.savantly.nexus.flow.dom.form.Forms;
import net.savantly.nexus.flow.dom.formSubmission.FormSubmissions;
import net.savantly.nexus.flow.dom.generator.GeneralGenerator;
import net.savantly.nexus.flow.dom.recaptcha.ReCaptchaAttemptService;
import net.savantly.nexus.flow.dom.recaptcha.ReCaptchaService;
import net.savantly.nexus.flow.executor.FlowExecutorFactory;
import net.savantly.nexus.flow.executor.FlowNodeFactory;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.kafka.dom.connection.KafkaTemplateFactory;
import net.savantly.nexus.kafka.dom.host.KafkaHosts;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecretRepository;
import net.savantly.nexus.organizations.dom.organizationSecret.OrganizationSecrets;
import net.savantly.nexus.webhooks.dom.webhook.Webhooks;

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
@ConfigurationProperties(prefix = "nexus.flow")
public class FlowModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.flow";
    public static final String SCHEMA = "flow";

    @Setter
    private String recaptchEndpoint = "https://www.google.com/recaptcha/api/siteverify";

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
    public WebhookDestinationHookFactory flow_webhookDestinationHookFactory(Webhooks webhooks,
            FlowContextFactory flowContextFactory, JavascriptExecutor javascriptExecutor,
            RepositoryService repositoryService, RestTemplateBuilder restTemplateBuilder,
            ObjectMapper objectMapper) {
        return new WebhookDestinationHookFactory(flowContextFactory, javascriptExecutor, repositoryService, webhooks,
                restTemplateBuilder, objectMapper);
    }

    @Bean
    public KafkaDestinationHookFactory flow_kafkaDestinationHookFactory(
            FlowContextFactory flowContextFactory, JavascriptExecutor javascriptExecutor,
            RepositoryService repositoryService, ObjectMapper objectMapper, KafkaTemplateFactory kafkaTemplateFactory,
            KafkaHosts kafkaHosts) {
        return new KafkaDestinationHookFactory(flowContextFactory, javascriptExecutor, repositoryService,
                objectMapper, kafkaTemplateFactory, kafkaHosts);
    }

    @Bean
    public DestinationHookFactory flow_destinationHookFactory(ObjectMapper objectMapper,
            DatasourceFactory datasourceFactory, JdbcConnections jdbcConnections, Webhooks webhooks,
            RestTemplateBuilder restTemplateBuilder, FlowDestinationHookFactory flowDestinationHookFactory,
            EmailDestinationHookFactory emailDestinationHookFactory,
            WebhookDestinationHookFactory webhookDestinationHookFactory,
            KafkaDestinationHookFactory kafkaDestinationHookFactory) {
        return new DestinationHookFactory(objectMapper, datasourceFactory, jdbcConnections,
                restTemplateBuilder, flowDestinationHookFactory, emailDestinationHookFactory,
                webhookDestinationHookFactory, kafkaDestinationHookFactory);
    }

    @Bean
    public DatasourceFactory flow_datasourceFactory(OrganizationSecrets flowSecrets) {
        return new DatasourceFactory(flowSecrets);
    }

    @Bean
    @Scope(scopeName = "prototype")
    public JavascriptExecutor flow_javascriptExecutor(IJavetEnginePool javetEnginePool) {
        return new JavascriptExecutor(() -> javetEnginePool);
    }

    @Bean
    public FlowNodeDiscoveryService flow_flowNodeDiscoveryService(FlowNodeSchemaGenerator flowNodeSchemaGenerator) {
        return new FlowNodeDiscoveryService(flowNodeSchemaGenerator);
    }

    @Bean
    public FlowNodeFactory flow_flowNodeFactory(JavascriptExecutor javascriptExecutor) {
        return new FlowNodeFactory(javascriptExecutor);
    }

    @Bean
    public FlowExecutorFactory flow_flowExecutorFactory(FlowNodeFactory nodeFactory) {
        return new FlowExecutorFactory(nodeFactory);
    }

    @Bean
    public FlowContextFactory flow_flowContextFactory(OrganizationSecrets flowSecrets) {
        return new FlowContextFactory(flowSecrets);
    }

    @Bean
    public FlowDefinitionExecutionProxy flow_flowDefinitionExecutionProxy(FlowExecutorFactory flowExecutorFactory,
            FlowDefinitionRepository flowDefinitionRepository, ObjectMapper objectMapper,
            RepositoryService repositoryService, OrganizationSecretRepository secretRepository,
            FlowContextFactory flowContextFactory) {
        return new FlowDefinitionExecutionProxy(flowExecutorFactory, flowDefinitionRepository, objectMapper,
                repositoryService, flowContextFactory);
    }

    @Bean
    public FlowService flow_flowService(FlowDefinitions flowDefinitions,
            FlowNodeDiscoveryService flowNodeDiscoveryService, Forms forms) {
        return new FlowService(flowDefinitions, flowNodeDiscoveryService, forms);
    }

    @Bean
    public FormSubmissionProxy flow_formSubmissionProxy(FormSubmissions formSubmissions, FormRepository formRepository,
            ObjectMapper objectMapper, DestinationHookFactory destinationHookFactory,
            ReCaptchaService recaptchaService) {
        return new FormSubmissionProxy(formSubmissions, formRepository, objectMapper, destinationHookFactory,
                recaptchaService);
    }

    @Bean
    public ReCaptchaAttemptService flow_reCaptchaAttemptService() {
        return new ReCaptchaAttemptService();
    }

    @Bean
    public ReCaptchaService flow_reCaptchaService(ReCaptchaAttemptService reCaptchaAttemptService) {
        return new ReCaptchaService(reCaptchaAttemptService, recaptchEndpoint);
    }

    @Bean
    public EmailDestinationHookFactory flow_emailDestinationHookFactory(EmailService emailService,
            EmailTargets emailTargets, JavascriptExecutor javascriptExecutor, FlowContextFactory flowContextFactory,
            RepositoryService repositoryService, ObjectMapper objectMapper) {
        return new EmailDestinationHookFactory(emailService, emailTargets, javascriptExecutor, flowContextFactory,
                repositoryService, objectMapper);
    }

}