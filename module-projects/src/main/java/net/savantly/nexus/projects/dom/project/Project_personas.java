package net.savantly.nexus.projects.dom.project;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersona;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaRepository;


@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Project_personas {

    final Project obj;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_personas> {
    }

    @Inject
    @Transient
    ProjectPersonaRepository repository;

    @CollectionLayout(named = "Personas", describedAs = "Personas for this project")
    public Set<ProjectPersona> coll() {
        return repository.findByProjectId(obj.getId());
    }
}
