package net.savantly.franchise.dom.location;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.causeway.applib.services.wrapper.DisabledException;
import org.apache.causeway.applib.services.wrapper.InvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import net.savantly.franchise.fixture.location.FranchiseLocation_persona;
import net.savantly.franchise.integtests.FranchiseModuleIntegTestAbstract;


@Transactional
public class FranchiseLocation_IntegTest extends FranchiseModuleIntegTestAbstract {

    FranchiseLocation franchiseLocation;

    @BeforeEach
    public void setUp() {
        // given
        franchiseLocation = fixtureScripts.runPersona(FranchiseLocation_persona.FOO);
    }


    @Nested
    public static class name extends FranchiseLocation_IntegTest {

        @Test
        public void accessible() {
            // when
            final String name = wrap(franchiseLocation).getName();

            // then
            assertThat(name).isEqualTo(franchiseLocation.getName());
        }

        @Test
        public void not_editable() {

            // expect
            assertThrows(DisabledException.class, ()->{

                // when
                wrap(franchiseLocation).setName("new name");
            });
        }

    }

    @Nested
    public static class updateName extends FranchiseLocation_IntegTest {


        @Test
        public void can_be_updated_directly() {

            // when
            wrap(franchiseLocation).updateName("new name");
            transactionService.flushTransaction();

            // then
            assertThat(wrap(franchiseLocation).getName()).isEqualTo("new name");
        }

        @Test
        public void fails_validation() {

            // expect
            InvalidException cause = assertThrows(InvalidException.class, ()->{

                // when
                wrap(franchiseLocation).updateName("new name!");
            });

            // then
            assertThat(cause.getMessage()).contains("Character '!' is not allowed");
        }
    }

}
