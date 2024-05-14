package net.savantly.ai.config;

import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.savantly.ai.languagetools.LanguageToolModel;

/**
 * See LangChain4J for more information.
 */
@Slf4j
@Configuration("aiConfig")
@ConfigurationProperties(prefix = "nexus.modules.ai")
@Data
public class AIConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired(required = false)
    OpenAiChatModel chatModel;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        log.info("AIConfig initialized");

        if (chatModel != null) {
            log.info("ChatLanguageModel: {}", chatModel);
        } else {
            log.warn("No ChatLanguageModel found");
        }
    }

    @Bean
    @ConditionalOnProperty("langchain4j.open-ai.chat-model.api-key")
    public LanguageToolModel languageToolModelBean(OpenAiChatModel chatModel) {

        log.info("Creating LanguageToolModel with OpenAiChatModel: {}", chatModel);

        return new LanguageToolModel() {

            @Override
            public Response<AiMessage> generate(List<ChatMessage> messages) {
                return chatModel.generate(messages);
            }

            @Override
            public Response<AiMessage> generate(List<ChatMessage> messages,
                    List<ToolSpecification> toolSpecifications) {
                return chatModel.generate(messages, toolSpecifications);
            }

            @Override
            public Response<AiMessage> generate(List<ChatMessage> messages, ToolSpecification toolSpecification) {
                return chatModel.generate(messages, toolSpecification);
            }

        };
    }

}