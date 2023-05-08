package net.savantly.franchise.fixture.brand;

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
import net.savantly.franchise.dom.brand.Brand;
import net.savantly.franchise.dom.brand.Brands;

@RequiredArgsConstructor
public enum Brand_persona
implements Persona<Brand, Brand_persona.Builder> {

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
    public Brand findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(Brands.class).map(x -> x.findByNameIgnoreCase(name)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<Brand> {
        @Getter @Setter private Brand_persona persona;

        @Override
        protected Brand buildResult(final ExecutionContext ec) {
            val object = service.create(persona.getId(), persona.name);
            object.setEmailAddress("support@savantly.net");
            return object;
        }

        // -- DEPENDENCIES
        @Inject Brands service;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Brand, Brand_persona, Builder> {
        public PersistAll() {
            super(Brand_persona.class);
        }
    }


}
