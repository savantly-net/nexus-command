package net.savantly.nexus.orgfees.dom.invoice;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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

@Named(OrganizationsModule.NAMESPACE + ".Invoice")
@javax.persistence.Entity
@javax.persistence.Table(schema = OrganizationsModule.SCHEMA)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "repeat")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Invoice extends AuditedEntity implements Comparable<Invoice> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Invoice withRequiredFields(String id, Organization organization, LocalDate startDate, LocalDate endDate) {
        val entity = new Invoice();
        entity.id = id;
        entity.setOrganization(organization);
        return entity;
    }

    public static Invoice withRequiredFields(Organization organization, LocalDate startDate, LocalDate endDate) {
        val id = UUID.randomUUID().toString();
        return withRequiredFields(id, organization, startDate, endDate);
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
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

    @Column(name = "start_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "4")
    @Getter
    private LocalDate startDate;

    @Column(name = "end_date")
    @PropertyLayout(fieldSetId = "identity", sequence = "5")
    @Getter
    private LocalDate endDate;


    // *** ACTIONS ***



    // *** IMPLEMENTATIONS ****

    private final static Comparator<Invoice> comparator = Comparator.comparing(s -> s.startDate);

    @Override
    public int compareTo(final Invoice other) {
        return comparator.compare(this, other);
    }

}
