package net.savantly.nexus.orgfees.dom.invoice;


import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

@Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class MonthlyOrgReport_generateInvoice {

    final MonthlyOrgReport object;

    public static class ActionEvent
            extends
            org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<MonthlyOrgReport_generateInvoice> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    MessageService messageService;

    @ActionLayout(named = "Create Invoice", promptStyle = PromptStyle.DIALOG)
    public Invoice act() {
        var invoice = Invoice.withRequiredFields(object);
        messageService.informUser("Generated " + invoice);
        return repositoryService.persist(invoice);
    }

}
