package net.savantly.nexus.command.web.fixture.pageBlock;



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
import net.savantly.nexus.command.web.dom.page.WebPages;
import net.savantly.nexus.command.web.dom.pageBlock.PageBlock;
import net.savantly.nexus.command.web.dom.pageBlock.PageBlocks;
import net.savantly.nexus.command.web.fixture.block.Block_persona;
import net.savantly.nexus.command.web.fixture.page.WebPage_persona;

@RequiredArgsConstructor
public enum PageBlock_persona
implements Persona<PageBlock, PageBlock_persona.Builder> {

    ABOUT_US_LINKS("about-us-links", WebPage_persona.ABOUT_US, Block_persona.CATEGORIZED_LINKS);

    private final String id;
    private final WebPage_persona webPagePersona;
    private final Block_persona blockPersona;

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public PageBlock findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(PageBlocks.class).map(x -> x.findById(id)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<PageBlock> {
        @Getter @Setter private PageBlock_persona persona;

        @Override
        protected PageBlock buildResult(final ExecutionContext ec) {
            val webPage = webPages.findById(persona.webPagePersona.getId());
            val block = blocks.findById(persona.blockPersona.getId());
            val object = service.create(persona.id, webPage, block);
            return object;
        }

        // -- DEPENDENCIES
        @Inject PageBlocks service;
        @Inject WebPages webPages;
        @Inject Blocks blocks;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<PageBlock, PageBlock_persona, Builder> {
        public PersistAll() {
            super(PageBlock_persona.class);
        }
    }


}
