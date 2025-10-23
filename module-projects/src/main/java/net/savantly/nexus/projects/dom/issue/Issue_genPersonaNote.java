package net.savantly.nexus.projects.dom.issue;

import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersona;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaRepository;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Issue_genPersonaNote {

    final Issue object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Issue_genDescription> {
    }

    @Inject
    @Transient
    IssueGenAi generator;
    @Inject
    @Transient
    ProjectPersonaRepository projectPersonaRepository;

    @ActionLayout(named = "Comment by persona", cssClassFa = "magic", associateWith = "notes", promptStyle = PromptStyle.DIALOG)
    public Issue act(
            @ParameterLayout(named = "Persona") ProjectPersona persona) {
        object.addNote(generator.generateIssueNote(object, persona));
        return object;
    }

    @MemberSupport
    public Set<ProjectPersona> choices0Act() {
        return projectPersonaRepository.findByProjectId(object.getProject().getId());
    }

}
