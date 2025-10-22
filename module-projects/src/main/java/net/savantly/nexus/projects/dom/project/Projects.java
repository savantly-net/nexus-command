package net.savantly.nexus.projects.dom.project;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.projects.ProjectsModule;


@Named(ProjectsModule.NAMESPACE + ".Projects")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = {@Inject} )
public class Projects {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final ProjectRepository repository;


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Project create(
            @Name final String name) {
        return repositoryService.persist(Project.withName(name));
    }

    @Programmatic
    public Project create(
            final String id,
            @Name final String name) {
        return repositoryService.persist(Project.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Project> listAll() {
        return repository.findAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public Project findByName(final Project group) {
        return group;
    }

    @Programmatic
    public Project findByNameIgnoreCase(final String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Programmatic
    public Project getById(String projectId) {
        return repository.getReferenceById(projectId);
    }
    
    public Collection<Project> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(Project.class)
            .ifSuccess(entityManager -> {
                final TypedQuery<Project> q = entityManager.get().createQuery(
                        "SELECT p FROM Projects p ORDER BY p.name",
                        Project.class)
                    .setMaxResults(1);
                q.getResultList();
            });
    }

}
