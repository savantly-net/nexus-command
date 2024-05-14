package net.savantly.nexus.projects.dom.issue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.project.Project;

@Named(ProjectsModule.NAMESPACE + ".Issues")
@DomainService(nature = NatureOfService.VIEW)
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Issues {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final IssueRepository repository;

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT)
    @ActionLayout(promptStyle = PromptStyle.DIALOG_SIDEBAR)
    public Issue create(
            @Name final String name,
            final Project project) {
        return repositoryService.persist(Issue.withRequiredFields(UUID.randomUUID().toString(), name, project));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Issue> listAll() {
        return repository.findAll();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public Issue findByName(final Issue group) {
        return group;
    }

    @Programmatic
    public Issue findByNameIgnoreCase(final String name) {
        return repository.findByNameIgnoreCase(name);
    }

    @Programmatic
    public Issue getById(String projectId) {
        return repository.getReferenceById(projectId);
    }

    public Collection<Issue> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(Issue.class)
                .ifSuccess(entityManager -> {
                    final TypedQuery<Issue> q = entityManager.get().createQuery(
                            "SELECT p FROM Issues p ORDER BY p.name",
                            Issue.class)
                            .setMaxResults(1);
                    q.getResultList();
                });
    }

}
