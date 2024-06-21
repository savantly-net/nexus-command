package net.savantly.nexus.webhooks.dom.userActions;

import org.apache.causeway.applib.services.registry.ServiceRegistry;
import org.apache.causeway.testing.integtestsupport.applib.validate.DomainModelValidator;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.webhooks.integtests.AbstractIntegrationTest;

@Log4j2
public class UserActionService_integTest extends AbstractIntegrationTest {

    @Inject ServiceRegistry serviceRegistry;

    @Inject
    UserActionService userActionService;

    @Test
    void validate() {
        new DomainModelValidator(serviceRegistry).assertValid();
    }

    @Test
    void visibleDomainMembersForUser() {
        var domainMembers = userActionService.visibleDomainMembersForUser();
        domainMembers.forEach(log::info);
    }

    @Test
    void getDomainActionClasses() {
        var domainMembers = userActionService.getDomainActionClasses();
        domainMembers.forEach(log::info);
    }


}
