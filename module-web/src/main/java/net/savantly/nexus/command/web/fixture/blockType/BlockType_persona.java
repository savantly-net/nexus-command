package net.savantly.nexus.command.web.fixture.blockType;



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
import net.savantly.nexus.command.web.dom.blockType.BlockType;
import net.savantly.nexus.command.web.dom.blockType.BlockTypes;

@RequiredArgsConstructor
public enum BlockType_persona
implements Persona<BlockType, BlockType_persona.Builder> {

    FOOTER("default-footer", "footer.json", "footer-ui.json", "The default footer block type"),
    CATEGORIZED_LINKS("default-categorized-links", "categorized-links.json", "categorized-links-ui.json", "The default categorized links block type");

    private final String id;
    private final String schemaFileName;
    private final String uiSchemaFileName;
    private final String description;

    public String getId() {
        return id;
    }

    @Override
    public Builder builder() {
        return new Builder().setPersona(this);
    }

    @Override
    public BlockType findUsing(final ServiceRegistry serviceRegistry) {
        return serviceRegistry.lookupService(BlockTypes.class).map(x -> x.getById(id)).orElseThrow();
    }

    @Accessors(chain = true)
    public static class Builder extends BuilderScriptWithResult<BlockType> {
        private static final String CLASS_FOLDER = "/net/savantly/nexus/command/web/fixture/blockType/";
        @Getter @Setter private BlockType_persona persona;

        @Override
        protected BlockType buildResult(final ExecutionContext ec) {

            var schemaFile = new ClassPathResource(CLASS_FOLDER + persona.schemaFileName);
            var uiSchemaFile = new ClassPathResource(CLASS_FOLDER + persona.uiSchemaFileName);
            val schema = getData(schemaFile);
            val uiSchema = getData(uiSchemaFile);
            val object = service.create(persona.id, persona.id);
            object.setDescription(persona.description);
            object.setPublishDate(ZonedDateTime.now());
            object.setSchema(schema);
            object.setUiSchema(uiSchema);
            return object;
        }

        private String getData(ClassPathResource file) {
            try {
                return new String(file.getInputStream().readAllBytes());
            } catch (IOException e) {
                throw new RuntimeException("failed to deserialize BlockTypeDto", e);
            }
        }

        // -- DEPENDENCIES
        @Inject BlockTypes service;
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<BlockType, BlockType_persona, Builder> {
        public PersistAll() {
            super(BlockType_persona.class);
        }
    }


}
