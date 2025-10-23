package net.savantly.nexus.products.dom.product;

import java.time.LocalDate;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;

import jakarta.inject.Inject;
import net.savantly.nexus.products.dom.productPrice.ProductPrices;

@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class Product_updatePrice {

    final Product object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Product_updatePrice> {
    }

    @Inject
    private ProductPrices productPrices;

    @MemberSupport
    public Product act(
            @ParameterLayout(named = "Price") final double price, LocalDate startDate) {

        var newPrice = productPrices.create(object, price, startDate);

        object.getPrices().add(newPrice);

        return object;
    }

}
