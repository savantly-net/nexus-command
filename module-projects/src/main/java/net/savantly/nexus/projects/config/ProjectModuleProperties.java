package net.savantly.nexus.projects.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "nexus.projects")
public class ProjectModuleProperties {
    
    private Prompts prompts = new Prompts();

    @Data
    public static class Prompts {
        private String project = "Project";
        private Issue issue = new Issue();
    }

    @Data
    public static class Issue {
        private String description = "Generate an issue description based on the project and context.";
        private String personaNote = "Act as the following persona: {{persona}}.\n\n write a comment given the project and issue context.";
    }
}
