package net.savantly.nexus.products.dom.product;

import java.util.Comparator;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.products.ProductsModule;
import net.savantly.nexus.products.dom.billing.BillingIntervalType;

@Named(ProductsModule.NAMESPACE + ".Product")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = ProductsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "gift")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Product implements Comparable<Product> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Product withRequiredFields(String id, String name, String description, BillingIntervalType billingInterval) {
        val entity = new Product();
        entity.id = id;
        entity.setName(name);
        entity.setDescription(description);
        entity.billingInterval = billingInterval;
        return entity;
    }

    public static Product withRequiredFields(String name, String description, BillingIntervalType billingInterval) {
        val id = UUID.randomUUID().toString();
        return withRequiredFields(id, name, description, billingInterval);
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @Title
    @Name
    @Column(name = "NAME", length = Name.MAX_LEN, nullable = false)
    @Property
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    private String name;


    @Column(length = 1000, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "3")
    @Getter
    @Setter
    private String description;


    @Column(name = "price", nullable = false)
    @Property
    @Getter
    @Setter
    @PropertyLayout(fieldSetId = "identity", sequence = "3")
    private double price;


    @Column(name = "billing_period", nullable = false)
    @Property
    @Getter
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    @Enumerated(EnumType.STRING)
    private BillingIntervalType billingInterval;


    // *** ACTIONS ***


    @Action
    @ActionLayout(named = "Update Name", associateWith = "name")
    public Product updateName(final String name) {
        this.name = name;
        return this;
    }

    @Action
    @ActionLayout(named = "Update Description", associateWith = "description")
    public Product updateDescription(final String description) {
        this.description = description;
        return this;
    }

    @Action
    @ActionLayout(named = "Update Price", associateWith = "price")
    public Product updatePrice(final double price) {
        this.price = price;
        return this;
    }

    @Action
    @ActionLayout(named = "Update Billing Interval" , associateWith = "billingInterval")
    public Product updateBillingInterval(final BillingIntervalType billingInterval) {
        this.billingInterval = billingInterval;
        return this;
    }

    // *** IMPLEMENTATIONS ****

    private final static Comparator<Product> comparator = Comparator.comparing(Product::getName);

    @Override
    public int compareTo(final Product other) {
        return comparator.compare(this, other);
    }

}
