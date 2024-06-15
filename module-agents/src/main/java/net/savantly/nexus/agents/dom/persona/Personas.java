package net.savantly.nexus.agents.dom.persona;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.MinLength;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.TypedQuery;
import net.savantly.nexus.agents.AgentsModule;
import net.savantly.nexus.common.types.Name;

@Named(AgentsModule.NAMESPACE + ".Personas")
@DomainService
@DomainServiceLayout()
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Personas {
    final JpaSupportService jpaSupportService;
    final PersonaRepository repository;

    @Programmatic
    public Persona create(
            final String id,
            @Name final String name) {
        return repository.save(Persona.withRequiredFields(id, name));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout()
    public List<Persona> listAll() {
        return repository.findAll();
    }

    @Programmatic
    public Persona getById(String projectId) {
        return repository.getReferenceById(projectId);
    }

    public Collection<Persona> autoComplete0FindByName(@MinLength(1) final String search) {
        if (Objects.isNull(search) || "".equals(search)) {
            return Collections.emptyList();
        }
        return repository.findByNameContainingIgnoreCase(search);
    }

    @Programmatic
    public void ping() {
        jpaSupportService.getEntityManager(Persona.class)
                .ifSuccess(entityManager -> {
                    final TypedQuery<Persona> q = entityManager.get().createQuery(
                            "SELECT p FROM Personas p ORDER BY p.name",
                            Persona.class)
                            .setMaxResults(1);
                    q.getResultList();
                });
    }

}
