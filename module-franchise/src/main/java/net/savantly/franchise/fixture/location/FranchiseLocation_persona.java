package net.savantly.franchise.fixture.location;


import javax.inject.Inject;

import org.apache.causeway.applib.services.clock.ClockService;
import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fakedata.applib.services.FakeDataService;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.dom.location.FranchiseLocations;

@RequiredArgsConstructor
public enum FranchiseLocation_persona
implements Persona<FranchiseLocation, FranchiseLocation_persona.Builder> {

    FOO("Foo", "example"),
    BAR("Bar", "example"),
    BAZ("Baz", null),
    FRODO("Frodo", "example"),
    FROYO("Froyo", null),
    FIZZ("Fizz", "example"),
    BIP("Bip", null),
    BOP("Bop", null),
    BANG("Bang", "example"),
    BOO("Boo", null);

    private final String name;
    private final String notes;

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public FranchiseLocation findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(FranchiseLocations.class).map(x -> x.findByNameExact(name)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<FranchiseLocation> {

        @Getter @Setter private FranchiseLocation_persona persona;

        @Override
        protected FranchiseLocation buildResult(final ExecutionContext ec) {

            val simpleObject = wrap(simpleObjects).create(null, persona.name);
            simpleObject.setNotes(persona.notes);

            return simpleObject;
        }

        // -- DEPENDENCIES

        @Inject FranchiseLocations simpleObjects;
        @Inject ClockService clockService;
        @Inject FakeDataService fakeDataService;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<FranchiseLocation, FranchiseLocation_persona, Builder> {
        public PersistAll() {
            super(FranchiseLocation_persona.class);
        }
    }


}
