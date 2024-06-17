package net.savantly.nexus.orgfees.dom.organization;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.invoice.Invoice;
import net.savantly.nexus.orgfees.dom.invoice.InvoiceRepository;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_invoices {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_invoices> {
    }

    @Inject
    @Transient
    InvoiceRepository repository;

    @CollectionLayout(named = "Invoices", describedAs = "Invoices for this organization")
    public Set<Invoice> coll() {
        return repository.findByOrganizationId(organization.getId());
    }
}
