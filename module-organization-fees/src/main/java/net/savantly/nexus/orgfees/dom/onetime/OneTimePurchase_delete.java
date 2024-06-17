package net.savantly.nexus.orgfees.dom.onetime;


import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class OneTimePurchase_delete {

    final OneTimePurchase object;

    public static class ActionEvent
            extends
            org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<OneTimePurchase_delete> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    MessageService messageService;

    @ActionLayout(named = "Delete OTP", associateWith = "purchases", promptStyle = PromptStyle.DIALOG)
    public Organization act() {
        var organization = object.getOrganization();
        repositoryService.remove(object);
        messageService.informUser("Deleted " + object);
        return organization;
    }

}
