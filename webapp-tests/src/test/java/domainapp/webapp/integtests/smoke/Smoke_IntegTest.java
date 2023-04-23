package domainapp.webapp.integtests.smoke;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.services.wrapper.InvalidException;
import org.apache.causeway.applib.services.xactn.TransactionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import domainapp.webapp.integtests.WebAppIntegTestAbstract;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.dom.location.FranchiseLocations;

@Transactional
class Smoke_IntegTest extends WebAppIntegTestAbstract {

    @Inject FranchiseLocations menu;
    @Inject TransactionService transactionService;

    @Test
    void happy_case() {

        // when
        List<FranchiseLocation> all = wrap(menu).listAll();

        // then
        assertThat(all).isEmpty();


        // when
        final FranchiseLocation fred = wrap(menu).create("Fred");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
        assertThat(all).contains(fred);


        // when
        final FranchiseLocation bill = wrap(menu).create("Bill");
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(2);
        assertThat(all).contains(fred, bill);


        // when
        wrap(fred).updateName("Freddy");
        transactionService.flushTransaction();

        // then
        assertThat(wrap(fred).getName()).isEqualTo("Freddy");


        // when
        wrap(fred).setNotes("These are some notes");
        transactionService.flushTransaction();

        // then
        assertThat(wrap(fred).getNotes()).isEqualTo("These are some notes");


        // when
        Assertions.assertThrows(InvalidException.class, () -> {
            wrap(fred).updateName("New name !!!");
            transactionService.flushTransaction();
        }, "Exclamation mark is not allowed");

        // then
        assertThat(wrap(fred).getNotes()).isEqualTo("These are some notes");


        // when
        wrap(fred).delete();
        transactionService.flushTransaction();

        // then
        all = wrap(menu).listAll();
        assertThat(all).hasSize(1);
    }

}

