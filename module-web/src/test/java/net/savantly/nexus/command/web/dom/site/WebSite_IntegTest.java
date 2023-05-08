package net.savantly.nexus.command.web.dom.site;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.savantly.nexus.command.web.fixture.site.WebSite_persona;
import net.savantly.nexus.command.web.integtests.NexusCommandWebModuleItegTestAbstract;


@Transactional
public class WebSite_IntegTest extends NexusCommandWebModuleItegTestAbstract {

    WebSite webSite;

    @BeforeEach
    public void setUp() {
        // given
        webSite = fixtureScripts.runPersona(WebSite_persona.SAVANTLY);
    }


    @Nested
    public static class name extends WebSite_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(webSite).getName();

            // then
            assertThat(name).isEqualTo(webSite.getName());
        }

    }

}
