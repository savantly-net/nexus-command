package net.savantly.nexus.franchise.dom.groupAddress;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Navigable;
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
import net.savantly.nexus.audited.api.AuditedEntity;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.franchise.dom.group.FranchiseGroup;

@Named(FranchiseModule.NAMESPACE + ".FranchiseGroupAddress")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
        schema=FranchiseModule.SCHEMA,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"group_id", "address_type"})
        }
    )
    @jakarta.persistence.EntityListeners(CausewayEntityListener.class)
    @DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
    @DomainObjectLayout()
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @XmlJavaTypeAdapter(PersistentEntityAdapter.class)
    @ToString(onlyExplicitlyIncluded = true)
public class FranchiseGroupAddress extends AuditedEntity {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static FranchiseGroupAddress withRequiredFields(FranchiseGroup parent, FranchiseGroupAddressType addressType) {
        val entity = new FranchiseGroupAddress();
        entity.setGroup(parent);
        entity.setAddressType(addressType);
        return entity;
    }

    // *** PROPERTIES ***

    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
    @Getter @Setter
    @JoinColumn(name = "group_id")
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "address", sequence = "1", navigable = Navigable.PARENT)
    private FranchiseGroup group;

    @Title
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    @PropertyLayout(fieldSetId = "address", sequence = "1.1")
    private FranchiseGroupAddressType addressType;

    @Getter @Setter
    @PropertyLayout(fieldSetId = "address", sequence = "1.2")
    private String emailAddress;

    
    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "1.3")
    @Getter @Setter @ToString.Include
    private String address1;

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "2")
    @Getter @Setter @ToString.Include
    private String address2;

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "3")
    @Getter @Setter @ToString.Include
    private String city;

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "4")
    @Getter @Setter @ToString.Include
    private String state;

    @Column(length = 20)
    @PropertyLayout(fieldSetId = "address", sequence = "5")
    @Getter @Setter @ToString.Include
    private String zip;
    
    
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        this.group.getAddresses().remove(this);
        repositoryService.removeAndFlush(this);
        messageService.informUser(String.format("'%s' deleted", title));
    }
}
