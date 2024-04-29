package net.savantly.ai.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * See LangChain4J for more information.
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "nexus.modules.ai")
@Data
public class AIConfig {

}