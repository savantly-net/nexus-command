package net.savantly.nexus.orgfees.dom.invoice;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.organizations.OrganizationsModule;

@Named(OrganizationsModule.NAMESPACE + ".InvoiceLineItem")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = OrganizationsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "file-lines", named = "Invoice Line Item")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class InvoiceLineItem implements Comparable<InvoiceLineItem> {

    public static InvoiceLineItem withRequiredFields(String id, Invoice invoice, LocalDate purchaseDate) {
        val entity = new InvoiceLineItem();
        entity.id = id;
        entity.invoice = invoice;
        entity.purchaseDate = purchaseDate;

        return entity;
    }

    public static InvoiceLineItem withRequiredFields(Invoice invoice, LocalDate purchaseDate) {
        val id = UUID.randomUUID().toString();
        return withRequiredFields(id, invoice, purchaseDate);
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

    @Getter
    @Property
    @PropertyLayout(fieldSetId = "identity", hidden = Where.REFERENCES_PARENT)
    @ManyToOne
    private Invoice invoice;

    @Column(name = "purchase_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    @Getter
    @Setter
    private LocalDate purchaseDate;

    @Column(name = "product_name")
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "2")
    private String productName;

    @Column(name = "product_description")
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "3")
    private String productDescription;

    @Column(name = "product_billing_interval")
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    private String productBillingInterval;

    @Column(name = "product_billing_amount", nullable = false)
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "5")
    private double productBillingAmount;

    @Column(name = "product_quantity", nullable = false)
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "6")
    private double productQuantity;

    @Column(name = "total_amount", nullable = false)
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "7")
    private double totalAmount;


    private final static Comparator<InvoiceLineItem> comparator = Comparator.comparing(s -> s.purchaseDate);

    @Override
    public int compareTo(final InvoiceLineItem other) {
        if (other == null || other.purchaseDate == null) {
            return 1;
        }
        return comparator.compare(this, other);
    }
}
