package net.savantly.nexus.projects.dom.project;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.nexus.projects.dom.generator.IssueGenerator;
import net.savantly.nexus.projects.dom.issue.Issue;

@Action
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Project_genPlanIssues {

    final Project project;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_genPlanIssues> {
    }

    @Inject
    @Transient
    IssueGenerator generator;
    @Inject
    @Transient
    RepositoryService repositoryService;


    @ActionLayout(named = "Generate issues from plan", cssClassFa = "magic", associateWith = "issues", describedAs = "Generate issues from the project plan", promptStyle = PromptStyle.DIALOG)
    public Project act() {
        var response = generator.generateIssues(formatContext());
        Set<Issue> issues = new HashSet<>();
        var tasks = response.getTasks();
        tasks.forEach(t -> {
            var issue = Issue.withRequiredFields(t.getName(), project);
            issue.setDescription(t.getDescription());
            issue.setLabels(t.getLabels());
            issues.add(issue);
        });
        project.getIssues().addAll(issues);
        return project;
    }

    @MemberSupport
    public String disableAct() {
        var isNullOrBlank = Objects.isNull(project.getPlan()) || project.getPlan().toString().isEmpty();
        return isNullOrBlank ? "Project plan is empty" : null;
    }

    private String formatContext() {
        var sb = new StringBuilder();
        sb.append(project.getPlan());
        return sb.toString();
    }
}
