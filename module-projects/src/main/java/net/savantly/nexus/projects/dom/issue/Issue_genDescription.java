package net.savantly.nexus.projects.dom.issue;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Issue_genDescription {

    final Issue object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Issue_genDescription> {
    }

    @Inject
    @Transient
    IssueGenAi generator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "AI", cssClassFa = "magic", associateWith = "description", promptStyle = PromptStyle.DIALOG)
    public Issue act() {
        final String text = generator.generateIssueDescription(object);
        object.setDescription(text);
        return object;
    }
}
