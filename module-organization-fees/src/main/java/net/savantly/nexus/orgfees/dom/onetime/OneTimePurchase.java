package net.savantly.nexus.orgfees.dom.onetime;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Navigable;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.api.OrganizationEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.products.dom.product.Product;

@Named(OrganizationsModule.NAMESPACE + ".OneTimePurchase")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = OrganizationsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "money")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class OneTimePurchase extends OrganizationEntity implements Comparable<OneTimePurchase> {


    public static OneTimePurchase withRequiredFields(String id, Product product, Organization organization) {
        val entity = new OneTimePurchase();
        entity.id = id;
        entity.setProduct(product);
        entity.setOrganization(organization);
        return entity;
    }

    public static OneTimePurchase withRequiredFields(Product product, Organization organization) {
        val id = UUID.randomUUID().toString();
        return withRequiredFields(id, product, organization);
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

    @Title(sequence = "1", append = " for ")
    @JoinColumn(name = "product_id", nullable = false)
    @Property
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private Product product;

    @Column(length = 1000, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "3")
    @Getter
    @Setter
    private String description;

    @Column(name = "purchase_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    @Getter
    @Setter
    private LocalDate purchaseDate;

    @Column(name = "purchase_quantity")
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    @Getter
    @Setter
    private double quantity;

    

    // *** IMPLEMENTATIONS ****

    private final static Comparator<OneTimePurchase> comparator = Comparator.comparing(s -> s.getProduct().getName());

    @Override
    public int compareTo(final OneTimePurchase other) {
        return comparator.compare(this, other);
    }

}
