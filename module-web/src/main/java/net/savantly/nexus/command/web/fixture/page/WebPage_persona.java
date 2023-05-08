package net.savantly.nexus.command.web.fixture.page;


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
import net.savantly.nexus.command.web.dom.page.WebPage;
import net.savantly.nexus.command.web.dom.page.WebPages;
import net.savantly.nexus.command.web.dom.site.WebSites;
import net.savantly.nexus.command.web.fixture.site.WebSite_persona;

@RequiredArgsConstructor
public enum WebPage_persona
implements Persona<WebPage, WebPage_persona.Builder> {

    ABOUT_US("about-us", "About Us", WebSite_persona.SAVANTLY);

    private final String id;
    private final String name;
    private final WebSite_persona site;

    public String getId() {
        return id;
    }
    
    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public WebPage findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(WebPages.class).map(x -> x.findById(id)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<WebPage> {
        @Getter @Setter private WebPage_persona persona;

        @Override
        protected WebPage buildResult(final ExecutionContext ec) {
            val webSite = webSites.findById(persona.site.getId());
            val object = service.create(persona.name, persona.id, webSite);
            return object;
        }

        // -- DEPENDENCIES
        @Inject WebPages service;
        @Inject WebSites webSites;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<WebPage, WebPage_persona, Builder> {
        public PersistAll() {
            super(WebPage_persona.class);
        }
    }


}
