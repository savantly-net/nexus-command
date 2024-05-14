package net.savantly.nexus.orgfees.dom.organization;

import java.util.Set;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Transient;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.subscription.Subscription;
import net.savantly.nexus.orgfees.dom.subscription.SubscriptionRepository;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Named("Organization_subscriptions")
public class Organization_subscriptions {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_subscriptions> {
    }

    @Inject
    @Transient
    SubscriptionRepository repository;

    @CollectionLayout(named = "Subscriptions", describedAs = "Subscriptions for this organization")
    public Set<Subscription> coll() {
        return repository.findByOrganizationId(organization.getId());
    }
}
