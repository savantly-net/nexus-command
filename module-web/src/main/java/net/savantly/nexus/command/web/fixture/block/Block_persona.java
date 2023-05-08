package net.savantly.nexus.command.web.fixture.block;

import java.io.IOException;
import java.time.ZonedDateTime;

import javax.inject.Inject;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.fixtures.applib.personas.BuilderScriptWithResult;
import org.apache.causeway.testing.fixtures.applib.personas.Persona;
import org.apache.causeway.testing.fixtures.applib.setup.PersonaEnumPersistAll;
import org.springframework.core.io.ClassPathResource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.val;
import lombok.experimental.Accessors;
import net.savantly.nexus.command.web.dom.block.Block;
import net.savantly.nexus.command.web.dom.block.Blocks;
import net.savantly.nexus.command.web.dom.blockType.BlockTypes;
import net.savantly.nexus.command.web.fixture.blockType.BlockType_persona;

@RequiredArgsConstructor
public enum Block_persona
        implements Persona<Block, Block_persona.Builder> {

    FOOTER("default-footer", BlockType_persona.FOOTER, "footer.json"),
    CATEGORIZED_LINKS("default-categorized-links", BlockType_persona.CATEGORIZED_LINKS, "categorized-links.json");

    private final String id;
    private final BlockType_persona blockTypePersona;
    private final String contentFileName;

    public String getId() {
        return id;
    }

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public Block findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(Blocks.class).map(x -> x.findById(id)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<Block> {
        private static final String CLASS_FOLDER = "/net/savantly/nexus/command/web/fixture/block/";
        @Getter
        @Setter
        private Block_persona persona;

        @Override
        protected Block buildResult(final ExecutionContext ec) {

            var blockType = blockTypes.getById(persona.blockTypePersona.getId());

            var contentFile = new ClassPathResource(
                    CLASS_FOLDER + persona.contentFileName);
            val content = getData(contentFile);
            val object = service.create(persona.id, persona.id, blockType);
            object.setPublishDate(ZonedDateTime.now());
            object.setContent(content);
            return object;
        }

        private String getData(ClassPathResource file) {
            try {
                return new String(file.getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("failed to deserialize BlockDto", e);
            }
        }

        // -- DEPENDENCIES
        @Inject
        Blocks service;
        @Inject
        BlockTypes blockTypes;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Block, Block_persona, Builder> {
        public PersistAll() {
            super(Block_persona.class);
        }
    }

}
