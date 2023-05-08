package net.savantly.nexus.command.web.dom.blockType;



import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.savantly.nexus.command.web.fixture.blockType.BlockType_persona;
import net.savantly.nexus.command.web.integtests.NexusCommandWebModuleItegTestAbstract;


@Transactional
public class BlockType_IntegTest extends NexusCommandWebModuleItegTestAbstract {

    BlockType entity;

    @BeforeEach
    public void setUp() {
        // given
        entity = fixtureScripts.runPersona(BlockType_persona.FOOTER);
    }


    @Nested
    public static class name extends BlockType_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(entity).getName();

            // then
            assertThat(name).isEqualTo(entity.getName());
        }

    }

}
