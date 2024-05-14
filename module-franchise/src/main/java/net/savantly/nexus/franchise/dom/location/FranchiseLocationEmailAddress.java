package net.savantly.nexus.franchise.dom.location;

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
import net.savantly.nexus.franchise.FranchiseModule;


@Named(FranchiseModule.NAMESPACE + ".FranchiseLocationEmailAddress")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
        schema=FranchiseModule.SCHEMA,
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"location_id", "address_type"})
        }
    )
    @jakarta.persistence.EntityListeners(CausewayEntityListener.class)
    @DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
    @DomainObjectLayout()
    @NoArgsConstructor(access = AccessLevel.PUBLIC)
    @XmlJavaTypeAdapter(PersistentEntityAdapter.class)
    @ToString(onlyExplicitlyIncluded = true)
public class FranchiseLocationEmailAddress {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static FranchiseLocationEmailAddress withRequiredFields(FranchiseLocation parent, FranchiseLocationEmailAddressType addressType, String emailAddress) {
        val entity = new FranchiseLocationEmailAddress();
        entity.setLocation(parent);
        entity.setAddressType(addressType);
        entity.setEmailAddress(emailAddress);
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
    @JoinColumn(name = "location_id")
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "address", sequence = "1", hidden = Where.PARENTED_TABLES, navigable = Navigable.PARENT)
    private FranchiseLocation location;

    @Title
    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "address_type")
    @PropertyLayout(fieldSetId = "address", sequence = "1.1")
    private FranchiseLocationEmailAddressType addressType;

    @Getter @Setter
    @PropertyLayout(fieldSetId = "address", sequence = "1.2")
    private String emailAddress;

    
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        this.getLocation().getEmailAddresses().remove(this);
        repositoryService.removeAndFlush(this);
        messageService.informUser(String.format("'%s' deleted", title));
    }
}
