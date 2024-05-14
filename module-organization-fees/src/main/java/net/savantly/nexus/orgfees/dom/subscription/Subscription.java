package net.savantly.nexus.orgfees.dom.subscription;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
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
import net.savantly.nexus.audited.dom.AuditedEntity;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.products.dom.product.Product;

@Named(OrganizationsModule.NAMESPACE + ".Subscription")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = OrganizationsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "repeat")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Subscription extends AuditedEntity implements Comparable<Subscription> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Subscription withRequiredFields(String id, Product product, Organization organization) {
        val entity = new Subscription();
        entity.id = id;
        entity.setProduct(product);
        entity.setOrganization(organization);
        return entity;
    }

    public static Subscription withRequiredFields(Product product, Organization organization) {
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
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    private Product product;

    @Title
    @JoinColumn(name = "organization_id")
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "identity", sequence = "2", navigable = Navigable.PARENT, hidden = Where.ALL_TABLES)
    private Organization organization;

    @Column(length = 1000, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "3")
    @Getter
    @Setter
    private String description;

    @Column(name = "start_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    @Getter
    private LocalDate startDate;

    @Column(name = "end_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "5")
    @Getter
    private LocalDate endDate;


    // *** ACTIONS ***
    @Action
    @ActionLayout(associateWith = "startDate", named = "Set Start Date")
    public Subscription setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    @Action
    @ActionLayout(associateWith = "endDate", named = "Set End Date")
    public Subscription setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }


    // *** IMPLEMENTATIONS ****

    private final static Comparator<Subscription> comparator = Comparator.comparing(s -> s.getProduct().getName());

    @Override
    public int compareTo(final Subscription other) {
        return comparator.compare(this, other);
    }

}
