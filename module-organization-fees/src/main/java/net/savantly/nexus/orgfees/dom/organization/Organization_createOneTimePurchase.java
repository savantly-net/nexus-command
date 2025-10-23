package net.savantly.nexus.orgfees.dom.organization;

import jakarta.inject.Inject;
import jakarta.persistence.Transient;

import java.time.LocalDate;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.services.repository.RepositoryService;

import io.swagger.v3.oas.annotations.Parameter;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.orgfees.dom.onetime.OneTimePurchase;
import net.savantly.nexus.products.dom.product.Product;

@Action
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
public class Organization_createOneTimePurchase {

    final Organization organization;

    public static class ActionEvent
            extends
            org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Organization_createOneTimePurchase> {
    }

    @Inject
    @Transient
    RepositoryService repositoryService;

    @ActionLayout(named = "Create OTP", associateWith = "purchases", promptStyle = PromptStyle.DIALOG)
    public OneTimePurchase act(
            @ParameterLayout(named = "Product") final Product product,
            @ParameterLayout(named = "Purchase Date") final LocalDate purchaseDate,
            @ParameterLayout(named = "Description") final String description,
            final double quantity) {
        final OneTimePurchase obj = OneTimePurchase.withRequiredFields(product, organization);
        obj.setDescription(description);
        obj.setPurchaseDate(purchaseDate);
        obj.setQuantity(quantity);
        return repositoryService.persist(obj);
    }

}
