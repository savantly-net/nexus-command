package net.savantly.nexus.projects.dom.issue;


import java.util.Set;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;

import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.projects.dom.generator.GeneralGenerator;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersona;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaRepository;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Issue_genPersonaNote {

    final Issue object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Issue_genDescription> {
    }

    @Inject
    @Transient
    GeneralGenerator generator;
    @Inject
    @Transient
    ProjectPersonaRepository projectPersonaRepository;

    final String system = "Act as the following persona: %s.\n\n write a comment given the project and issue context.";

    @ActionLayout(named = "Comment by persona", cssClassFa = "magic", associateWith = "notes", promptStyle = PromptStyle.DIALOG)
    public Issue act(
        @ParameterLayout(named = "Persona") ProjectPersona persona) {
        final String text = generator.generateText(String.format(system, persona.getPrompt()), formatContext());
        object.addNote(String.format("Persona: %s\n%s", persona.getPersona().getName(), text));
        return object;
    }

    @MemberSupport
    public Set<ProjectPersona> choices0Act() {
        return projectPersonaRepository.findByProjectId(object.getProject().getId());
    }

    private String formatContext() {
        var projectPrompt = PromptBuilder.format(object.getProject());
        var sb = new StringBuilder();
        sb.append("Issue: ");
        sb.append(object.getName());
        sb.append("\n");
        sb.append(object.getDescription());
        sb.append("\n");
        sb.append(projectPrompt);
        return sb.toString();
    }
}
