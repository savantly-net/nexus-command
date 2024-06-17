package net.savantly.nexus.orgfees.dom.invoice;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Navigable;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.audited.dom.AuditedEntity;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(OrganizationsModule.NAMESPACE + ".Invoice")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = OrganizationsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "repeat")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Invoice extends AuditedEntity implements Comparable<Invoice> {

    public static Invoice withRequiredFields(String id, Organization organization, LocalDate startDate, LocalDate endDate) {
        val entity = new Invoice();
        entity.id = id;
        entity.setOrganization(organization);
        entity.startDate = startDate;
        entity.endDate = endDate;
        return entity;
    }

    public static Invoice withRequiredFields(Organization organization, LocalDate startDate, LocalDate endDate) {
        val id = UUID.randomUUID().toString().substring(0, 16);
        return withRequiredFields(id, organization, startDate, endDate);
    }

    public static Invoice withRequiredFields(MonthlyOrgReport report) {
        var id = UUID.randomUUID().toString().substring(0, 16);
        var startDate = LocalDate.of(report.getYear(), report.getMonth().getValue(), 1);
        var endDate = startDate.plusMonths(1).minusDays(1);
        var entity = withRequiredFields(id, report.getOrganization(), startDate, endDate);

        report.getLineItems().forEach(li -> {
            var lineItem = InvoiceLineItem.withRequiredFields(entity, toLocalDate(li.getProductPurchaseDate()));
            lineItem.setProductBillingAmount(li.getProductBillingAmount());
            lineItem.setProductName(li.getProductName());
            lineItem.setProductQuantity(li.getProductQuantity());
            lineItem.setProductDescription(li.getProductDescription());
            lineItem.setTotalAmount(li.getTotalAmount());
            entity.getLineItems().add(lineItem);
        });
        return entity;
    }

    private static LocalDate toLocalDate(String stringDate) {
        return LocalDate.parse(stringDate);
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
    @JoinColumn(name = "organization_id")
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "identity", sequence = "2", navigable = Navigable.PARENT, hidden = Where.ALL_TABLES)
    private Organization organization;

    @Column(name = "invoice_year")
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    @Getter
    private LocalDate startDate;

    @Column(name = "end_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "5")
    @Getter
    private LocalDate endDate;

    @Collection
    @Getter
    @PropertyLayout(fieldSetId = "lineItems", sequence = "1")
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "invoice_id")
    private Set<InvoiceLineItem> lineItems = new java.util.TreeSet<>();

    @Transient
    public double getTotalAmount() {
        return getLineItems().stream().mapToDouble(InvoiceLineItem::getTotalAmount).sum();
    }


    // *** ACTIONS ***



    // *** IMPLEMENTATIONS ****

    private final static Comparator<Invoice> comparator = Comparator.comparing(s -> s.startDate);

    @Override
    public int compareTo(final Invoice other) {
        return comparator.compare(this, other);
    }

}
