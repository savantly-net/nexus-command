package net.savantly.nexus.orgfees.dom.organization;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Transient;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.onetime.OneTimePurchase;
import net.savantly.nexus.orgfees.dom.onetime.OneTimePurchaseRepository;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Named("Organization_purchases")
public class Organization_purchases {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_purchases> {
    }

    @Inject
    @Transient
    OneTimePurchaseRepository repository;

    @CollectionLayout(named = "Purchases", describedAs = "Purchases by this organization")
    public Set<OneTimePurchase> coll() {
        return repository.findByOrganizationId(organization.getId());
    }
}
