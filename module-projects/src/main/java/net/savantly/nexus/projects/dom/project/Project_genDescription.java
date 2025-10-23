package net.savantly.nexus.projects.dom.project;

import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.nexus.projects.dom.generator.GeneralGenerator;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Project_genDescription {

    final Project project;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_genDescription> {
    }

    @Inject
    @Transient
    GeneralGenerator generator;
    @Inject
    @Transient
    RepositoryService repositoryService;

    final String system = "Write an improved concise project description given the context.";

    @ActionLayout(named = "Rewrite", cssClassFa = "magic", associateWith = "description", describedAs = "Improve the description", promptStyle = PromptStyle.DIALOG)
    public Project act(@ParameterLayout(named = "Instruction", multiLine = 5) String instruction) {
        var text = generator.generateText(instruction, project.getDescription());
        project.setDescription(text);
        return project;
    }

    @MemberSupport
    public String default0Act() {
        return system;
    }

    @MemberSupport
    public String disableAct() {
        var isNullOrBlank = Objects.isNull(project.getDescription()) || project.getDescription().isBlank();
        return isNullOrBlank ? "Description is empty" : null;
    }
}
