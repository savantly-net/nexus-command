package net.savantly.nexus.projects.dom.project;

import java.util.Objects;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.valuetypes.markdown.applib.value.Markdown;

import net.savantly.nexus.projects.dom.generator.GeneralGenerator;

@Action
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Project_genPlan {

    final Project project;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_genPlan> {
    }

    @Inject
    @Transient
    GeneralGenerator generator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    final String system = "Write a project plan for the project with the given context. Use Markdown formatting.";

    @ActionLayout(named = "Generate Plan", cssClassFa = "magic", associateWith = "plan", describedAs = "Generate a project plan", promptStyle = PromptStyle.DIALOG)
    public Project act() {
        var text = generator.generateText(system, project.getDescription());
        project.setPlan(Markdown.valueOf(text));
        return project;
    }

    @MemberSupport
    public String disableAct() {
        var isNullOrBlank = Objects.isNull(project.getDescription()) || project.getDescription().isBlank();
        return isNullOrBlank ? "Description is empty" : null;
    }
}
