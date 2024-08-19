package net.savantly.nexus.webhooks;

import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.applib.services.metamodel.MetaModelService;
import org.apache.causeway.persistence.jpa.applib.CausewayModulePersistenceJpaApplib;
import org.apache.causeway.testing.fixtures.applib.fixturescripts.FixtureScript;
import org.apache.causeway.testing.fixtures.applib.modules.ModuleWithFixtures;
import org.apache.causeway.testing.fixtures.applib.teardown.jpa.TeardownFixtureJpaAbstract;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.savantly.nexus.webhooks.api.WebhookExecutor;
import net.savantly.nexus.webhooks.dom.eventFilter.EventFilter;
import net.savantly.nexus.webhooks.dom.listeners.OnCommandListener;
import net.savantly.nexus.webhooks.dom.userActions.UserActionService;
import net.savantly.nexus.webhooks.dom.webhookExecutor.DefaultWebhookExecutor;
import net.savantly.nexus.webhooks.dom.webhooktrigger.WebhookTriggerRespository;

@Configuration
@Import({
        CausewayModulePersistenceJpaApplib.class,
})
@ComponentScan
@EnableJpaRepositories
@Slf4j
@EntityScan(basePackageClasses = { WebhooksModule.class })
@RequiredArgsConstructor
@DependsOn("aiConfig")
public class WebhooksModule implements ModuleWithFixtures {

    public static final String NAMESPACE = "nexus.webhooks";
    public static final String SCHEMA = "webhooks";

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
    public UserActionService userActionService(MetaModelService metaModelService) {
        return new UserActionService(metaModelService);
    }

    @Bean
    public OnCommandListener onCommandListener(EventFilter eventFilter, WebhookExecutor webhookExecutor,
            BookmarkService bookmarkService, ObjectMapper objectMapper) {
        return new OnCommandListener(eventFilter, webhookExecutor, bookmarkService, objectMapper);
    }

    @Bean
    public EventFilter eventFilter(WebhookTriggerRespository webhookTriggerRespository) {
        return new EventFilter(webhookTriggerRespository);
    }

    @Bean
    public WebhookExecutor webhookExecutor(RestTemplateBuilder restTemplateBuilder) {
        return new DefaultWebhookExecutor(restTemplateBuilder);
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        return mapper;
    }

}