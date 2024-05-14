package net.savantly.nexus.command.web.fixture.siteBlock;


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
import net.savantly.nexus.command.web.dom.block.Blocks;
import net.savantly.nexus.command.web.dom.site.WebSites;
import net.savantly.nexus.command.web.dom.siteBlock.SiteBlock;
import net.savantly.nexus.command.web.dom.siteBlock.SiteBlocks;
import net.savantly.nexus.command.web.fixture.block.Block_persona;
import net.savantly.nexus.command.web.fixture.site.WebSite_persona;

@RequiredArgsConstructor
public enum SiteBlock_persona
implements Persona<SiteBlock, SiteBlock_persona.Builder> {

    SAVANTLY_HERO("savantly-site-hero", WebSite_persona.SAVANTLY, Block_persona.HERO),
    SAVANTLY_FOOTER("savantly-site-footer", WebSite_persona.SAVANTLY, Block_persona.FOOTER);

    private final String id;
    private final WebSite_persona webSitePersona;
    private final Block_persona blockPersona;

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public SiteBlock findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(SiteBlocks.class).map(x -> x.findById(id)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<SiteBlock> {
        @Getter @Setter private SiteBlock_persona persona;

        @Override
        protected SiteBlock buildResult(final ExecutionContext ec) {
            val webSite = webSites.findById(persona.webSitePersona.getId());
            val block = blocks.findById(persona.blockPersona.getId());
            val object = service.create(persona.id, webSite, block);
            return object;
        }

        // -- DEPENDENCIES
        @Inject SiteBlocks service;
        @Inject WebSites webSites;
        @Inject Blocks blocks;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<SiteBlock, SiteBlock_persona, Builder> {
        public PersistAll() {
            super(SiteBlock_persona.class);
        }
    }


}
