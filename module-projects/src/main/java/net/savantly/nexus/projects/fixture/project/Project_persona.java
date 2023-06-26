package net.savantly.nexus.projects.fixture.project;

import javax.inject.Inject;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;
import net.savantly.nexus.projects.dom.project.Project;
import net.savantly.nexus.projects.dom.project.Projects;

@RequiredArgsConstructor
public enum Project_persona
implements Persona<Project, Project_persona.Builder> {

    SAVANTLY("Savantly");

    private final String name;

    public String getId() {
        return name.toLowerCase();
    }

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public Project findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(Projects.class).map(x -> x.findByNameIgnoreCase(name)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<Project> {
        @Getter @Setter private Project_persona persona;

        @Override
        protected Project buildResult(final ExecutionContext ec) {
            val object = service.create(persona.getId(), persona.name);
            return object;
        }

        // -- DEPENDENCIES
        @Inject Projects service;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Project, Project_persona, Builder> {
        public PersistAll() {
            super(Project_persona.class);
        }
    }


}
