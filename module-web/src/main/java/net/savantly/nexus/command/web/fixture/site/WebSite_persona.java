package net.savantly.nexus.command.web.fixture.site;


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
import net.savantly.nexus.command.web.dom.site.WebSite;
import net.savantly.nexus.command.web.dom.site.WebSites;

@RequiredArgsConstructor
public enum WebSite_persona
implements Persona<WebSite, WebSite_persona.Builder> {

    SAVANTLY("savantly", "Savantly");

    private final String id;
    private final String name;

    public String getId() {
        return id;
    }

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public WebSite findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(WebSites.class).map(x -> x.findById(id)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<WebSite> {
        @Getter @Setter private WebSite_persona persona;

        @Override
        protected WebSite buildResult(final ExecutionContext ec) {

            val object = service.create(persona.id, persona.name);
            object.setUrl("https://savantly.net");
            return object;
        }

        // -- DEPENDENCIES
        @Inject WebSites service;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<WebSite, WebSite_persona, Builder> {
        public PersistAll() {
            super(WebSite_persona.class);
        }
    }



}
