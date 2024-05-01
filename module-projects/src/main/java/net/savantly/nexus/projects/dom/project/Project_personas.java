package net.savantly.nexus.projects.dom.project;

import java.util.Set;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Where;

import net.savantly.nexus.projects.dom.projectPersona.ProjectPersona;
import net.savantly.nexus.projects.dom.projectPersona.ProjectPersonaRepository;


@Collection
@CollectionLayout(hidden = Where.NOWHERE)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Project_personas {

    final Project obj;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Project_personas> {
    }

    @Inject
    @Transient
    ProjectPersonaRepository repository;

    @ActionLayout(named = "List Personas", describedAs = "Personas for this project")
    public Set<ProjectPersona> coll() {
        return repository.findByProjectId(obj.getId());
    }
}
