package net.savantly.nexus.command.web.dom.block;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.savantly.nexus.command.web.fixture.block.Block_persona;
import net.savantly.nexus.command.web.fixture.blockType.BlockType_persona;
import net.savantly.nexus.command.web.integtests.NexusCommandWebModuleItegTestAbstract;


@Transactional
public class Block_IntegTest extends NexusCommandWebModuleItegTestAbstract {

    Block entity;

    @BeforeEach
    public void setUp() {
        // given
        fixtureScripts.runPersona(BlockType_persona.FOOTER);
        entity = fixtureScripts.runPersona(Block_persona.FOOTER);
    }


    @Nested
    public static class name extends Block_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(entity).getName();

            // then
            assertThat(name).isEqualTo(entity.getName());
        }

    }

}
