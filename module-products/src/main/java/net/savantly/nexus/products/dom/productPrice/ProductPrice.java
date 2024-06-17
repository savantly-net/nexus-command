package net.savantly.nexus.products.dom.productPrice;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.products.ProductsModule;
import net.savantly.nexus.products.dom.product.Product;

@Named(ProductsModule.NAMESPACE + ".ProductPrice")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = ProductsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "dollar-sign")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class ProductPrice implements Comparable<ProductPrice> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static ProductPrice withRequiredFields(Product product, double price, LocalDate startDate) {
        val entity = new ProductPrice();
        entity.id = UUID.randomUUID().toString();
        entity.product = product;
        entity.price = price;
        entity.startDate = startDate;
        return entity;
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999", hidden = Where.PARENTED_TABLES)
    @Getter
    @Setter
    private long version;

    @JoinColumn(name = "product_id", nullable = false)
    @Getter
    @Setter
    private Product product;

    @Title
    @Column(name = "start_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    @Getter
    @Setter
    private LocalDate startDate;

    @Column(name = "price", nullable = false)
    @Property
    @Getter
    @Setter
    @PropertyLayout(fieldSetId = "identity", sequence = "2")
    private double price;

    @Column(length = 1000, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "3")
    @Getter
    @Setter
    private String description;

    // *** ACTIONS ***

    @Action
    @ActionLayout(named = "Update Description", associateWith = "description")
    public ProductPrice updateDescription(final String description) {
        this.description = description;
        return this;
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(named = "Update Price", associateWith = "price")
    public ProductPrice updatePrice(final double price) {
        this.price = price;
        return this;
    }

    // *** IMPLEMENTATIONS ****

    private final static Comparator<ProductPrice> comparator = Comparator.comparing(ProductPrice::getId);

    @Override
    public int compareTo(final ProductPrice other) {
        return comparator.compare(this, other);
    }

}
