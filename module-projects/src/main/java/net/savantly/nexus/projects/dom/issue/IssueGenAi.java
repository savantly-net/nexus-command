package net.savantly.nexus.projects.dom.issue;

import lombok.RequiredArgsConstructor;
import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.projects.config.ProjectModuleProperties;
import net.savantly.nexus.projects.dom.generator.GeneralGenerator;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersona;

@RequiredArgsConstructor
public class IssueGenAi {

    private final ProjectModuleProperties properties;
    private final GeneralGenerator generator;

    public String generateIssueDescription(Issue object) {

        var sb = new StringBuilder();
        sb.append(object.getName());
        sb.append("\n");
        sb.append(PromptBuilder.format(object.getProject()));
        var context = sb.toString();

        final String systemPrompt = properties.getPrompts().getIssue().getDescription();

        return generator.generateText(systemPrompt, context);
    }

    public String generateIssueNote(Issue object, ProjectPersona persona) {

        var sb = new StringBuilder();
        sb.append(object.getName());
        sb.append("\n");
        sb.append(object.getDescription());
        sb.append("\n");
        sb.append(PromptBuilder.format(object.getProject()));
        var context = sb.toString();

        final String systemPrompt = properties.getPrompts().getIssue().getPersonaNote()
                .replaceAll("\\{\\{persona\\}\\}", persona.getPrompt());

        final String text = generator.generateText(systemPrompt, context);
        return String.format("Persona: %s\n%s", persona.getPersona().getName(), text);
    }

}
