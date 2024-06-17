package net.savantly.nexus.products.dom.productPrice;

import java.time.LocalDate;
import java.util.List;

import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.DomainServiceLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.persistence.jpa.applib.services.JpaSupportService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import net.savantly.nexus.products.ProductsModule;
import net.savantly.nexus.products.dom.product.Product;

@Named(ProductsModule.NAMESPACE + ".ProductPrices")
@DomainService
@DomainServiceLayout(
    menuBar = DomainServiceLayout.MenuBar.PRIMARY
)
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ProductPrices {
    final RepositoryService repositoryService;
    final JpaSupportService jpaSupportService;
    final ProductPriceRepository repository;

    @Programmatic
    public ProductPrice create(
            final Product product, final double price, LocalDate startDate) {
        return repositoryService.persist(ProductPrice.withRequiredFields(product, price, startDate));
    }

    @Programmatic
    public List<ProductPrice> listAllByProductId(String productId) {
        return repository.findAllByProductId(productId);
    }

}
