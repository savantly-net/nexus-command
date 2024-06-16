package net.savantly.nexus.flow.dom.flowNodeSchema;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlowNodeSchemaConfiguration {
    
    @Bean
    public FlowNodeSchemaGenerator flowNodeSchemaGenerator() {
        return new FlowNodeSchemaGenerator();
    }
    
}
