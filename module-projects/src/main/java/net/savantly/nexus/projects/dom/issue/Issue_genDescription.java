package net.savantly.nexus.projects.dom.issue;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.projects.dom.generator.GeneralGenerator;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Issue_genDescription {

    final Issue object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Issue_genDescription> {
    }

    @Inject
    @Transient
    GeneralGenerator generator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    final String system = "Generate an issue description based on the project and context.";

    @ActionLayout(named = "AI", cssClassFa = "magic", associateWith = "description", promptStyle = PromptStyle.DIALOG)
    public Issue act() {
        final String text = generator.generateText(system, formatContext());
        object.setDescription(text);
        return object;
    }

    private String formatContext() {
        var projectPrompt = PromptBuilder.format(object.getProject());
        var sb = new StringBuilder();
        sb.append(object.getName());
        sb.append("\n");
        sb.append(projectPrompt);
        return sb.toString();
    }
}
