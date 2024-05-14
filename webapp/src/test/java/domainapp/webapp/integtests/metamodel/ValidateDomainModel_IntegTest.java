package domainapp.webapp.integtests.metamodel;

import jakarta.inject.Inject;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.integtestsupport.applib.validate.DomainModelValidator;
import org.junit.jupiter.api.Test;

import domainapp.webapp.integtests.WebAppIntegTestAbstract;

class ValidateDomainModel_IntegTest extends WebAppIntegTestAbstract {

    @Inject ServiceRegistry serviceRegistry;

    @Test
    void validate() {
        new DomainModelValidator(serviceRegistry).assertValid();
    }


}
