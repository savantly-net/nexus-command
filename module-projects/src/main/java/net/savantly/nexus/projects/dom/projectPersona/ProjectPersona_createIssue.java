package net.savantly.nexus.projects.dom.projectPersona;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.projects.dom.generator.GeneralGenerator;
import net.savantly.nexus.projects.dom.issue.Issue;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class ProjectPersona_createIssue {

    final ProjectPersona object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<ProjectPersona> {
    }

    @Inject
    @Transient
    GeneralGenerator generator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    final String system = "Generate an issue description based on the project and context, using the defined persona.";

    @ActionLayout(named = "Create Issue Using Persona", cssClassFa = "magic", promptStyle = PromptStyle.DIALOG)
    public Issue act(
            @ParameterLayout(named = "Description", multiLine = 2) final String description) {
        var text = generator.generateText(system, formatContext(description));
        var issueName = generator.generateText("Create a name for the issue description", text);
        var issue = Issue.withRequiredFields(issueName, object.getProject());
        issue.setDescription(text);
        repositoryService.persist(issue.getProject());
        return repositoryService.persist(issue);
    }

    private String formatContext(String description) {
        var basePrompt = PromptBuilder.format(object.getProject());
        var sb = new StringBuilder();
        sb.append(basePrompt);
        sb.append("\n");
        sb.append("Issue Description: ");
        sb.append(description);
        return sb.toString();
    }
}
