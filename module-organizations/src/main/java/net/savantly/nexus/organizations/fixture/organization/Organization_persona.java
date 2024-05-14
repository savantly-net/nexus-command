package net.savantly.nexus.organizations.fixture.organization;

import jakarta.inject.Inject;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organization.Organizations;

@RequiredArgsConstructor
public enum Organization_persona
implements Persona<Organization, Organization_persona.Builder> {

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
    public Organization findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(Organizations.class).map(x -> x.findByNameIgnoreCase(name)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<Organization> {
        @Getter @Setter private Organization_persona persona;

        @Override
        protected Organization buildResult(final ExecutionContext ec) {
            val object = service.create(persona.getId(), persona.name);
            object.setEmailAddress("support@savantly.net");
            object.setCity("Fort Worth");
            object.setState("TX");
            object.setCountry("USA");
            object.setNotes("Savantly LLC is a software development company specializing in AI and enterprise software." +
                    " LOBs, ERPs, CRMs, Web Sites, and more. " +
                    "They are especially good at scaled architecture and process automation.");
            return object;
        }

        // -- DEPENDENCIES
        @Inject Organizations service;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Organization, Organization_persona, Builder> {
        public PersistAll() {
            super(Organization_persona.class);
        }
    }


}
