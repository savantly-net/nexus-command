package net.savantly.nexus.kafka;

import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.metamodel.MetaModelService;
import org.apache.causeway.persistence.jpa.applib.CausewayModulePersistenceJpaApplib;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.causeway.testing.fixtures.applib.teardown.jpa.TeardownFixtureJpaAbstract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.savantly.nexus.kafka.api.KafkaHookExecutor;
import net.savantly.nexus.kafka.dom.connection.KafkaTemplateFactory;
import net.savantly.nexus.kafka.dom.eventFilter.KafkaEventFilter;
import net.savantly.nexus.kafka.dom.hookExecutor.DefaultKafkaHookExecutor;
import net.savantly.nexus.kafka.dom.hookTrigger.KafkaHookTriggerRespository;
import net.savantly.nexus.kafka.dom.listeners.KafkaOnCommandListener;
import net.savantly.nexus.kafka.dom.userActions.KafkaUserActionService;

@Configuration
@Import({
        CausewayModulePersistenceJpaApplib.class,
})
@ComponentScan
@EnableJpaRepositories
@Slf4j
@EntityScan(basePackageClasses = { KafkaModule.class })
@RequiredArgsConstructor
@DependsOn("aiConfig")
public class KafkaModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.kafka";
    public static final String SCHEMA = "kafka";

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
    public KafkaUserActionService kafkaUserActionService(MetaModelService metaModelService) {
        return new KafkaUserActionService(metaModelService);
    }

    @Bean
    public KafkaOnCommandListener kafkaOnCommandListener(KafkaEventFilter eventFilter,
            KafkaHookExecutor kafkaHookExecutor, ObjectMapper objectMapper, InteractionService interactionService) {
        return new KafkaOnCommandListener(eventFilter, kafkaHookExecutor, objectMapper,
                interactionService);
    }

    @Bean
    public KafkaEventFilter kafkaEventFilter(KafkaHookTriggerRespository kafkaHookTriggerRespository) {
        return new KafkaEventFilter(kafkaHookTriggerRespository);
    }

    @Bean
    public KafkaTemplateFactory kafkaTemplateFactory() {
        return new KafkaTemplateFactory();
    }

    @Bean
    public KafkaHookExecutor kafkaHookExecutor(KafkaTemplateFactory kafkaTemplateFactory) {
        return new DefaultKafkaHookExecutor(kafkaTemplateFactory);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

}