package net.savantly.ai;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import net.savantly.ai.config.AIConfig;

@Configuration
@Import(AIConfig.class)
public class AIModule {
    
}
