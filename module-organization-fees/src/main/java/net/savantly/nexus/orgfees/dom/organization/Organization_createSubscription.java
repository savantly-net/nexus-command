package net.savantly.nexus.orgfees.dom.organization;

import javax.inject.Inject;
import javax.persistence.Transient;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.subscription.Subscription;
import net.savantly.nexus.products.dom.product.Product;

@Action
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Organization_createSubscription {

    final Organization organization;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_createSubscription> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Create Subscription", associateWith = "Organization_subscriptions", promptStyle = PromptStyle.DIALOG)
    public Subscription act(
            @ParameterLayout(named = "Product") final Product product) {
        final Subscription subscription = Subscription.withRequiredFields(product, organization);
        return repositoryService.persist(subscription);
    }

}
