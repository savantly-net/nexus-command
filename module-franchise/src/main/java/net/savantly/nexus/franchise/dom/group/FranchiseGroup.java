package net.savantly.nexus.franchise.dom.group;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
import net.savantly.nexus.common.types.EmailAddress;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.common.types.Notes;
import net.savantly.nexus.common.types.PhoneNumber;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.franchise.dom.groupAddress.FranchiseGroupAddress;
import net.savantly.nexus.franchise.dom.groupAddress.FranchiseGroupAddressType;
import net.savantly.nexus.franchise.dom.location.FranchiseLocation;
import net.savantly.nexus.franchise.dom.location.FranchiseLocations;
import net.savantly.nexus.organizations.api.OrganizationEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;

@Named(FranchiseModule.NAMESPACE + ".FranchiseGroup")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
	schema=FranchiseModule.SCHEMA,
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "franchisegroup__name__UNQ", columnNames = {"NAME"})
    }
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "building")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FranchiseGroup extends OrganizationEntity implements Comparable<FranchiseGroup>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    @Inject @Transient FranchiseLocations franchiseLocations;
    
    public static FranchiseGroup withName(Organization organization, String name) {
        val entity = new FranchiseGroup();
        entity.setName(name);
        entity.setOrganization(organization);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
    @Name
    @Column(length = Name.MAX_LEN, nullable = false)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private String name;

    @PhoneNumber
    @Column(length = PhoneNumber.MAX_LEN, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "1.5")
    @Getter @Setter
	private String phoneNumber;

    @EmailAddress
    @Column(length = EmailAddress.MAX_LEN, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "1.6")
    @Getter @Setter
	private String emailAddress;

    @Notes
    @Column(length = Notes.MAX_LEN, nullable = true)
    @PropertyLayout(fieldSetId = "other", sequence = "2")
    @Getter @Setter
	private String notes;

    

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "1")
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


    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "6")
    @Getter @Setter @ToString.Include
	private String county;

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "7")
    @Getter @Setter @ToString.Include
	private String country;
    
    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "8")
    @Getter @Setter
	private BigDecimal lat;

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "9")
    @Getter @Setter
	private BigDecimal lon;

    
    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "group")
	private Set<FranchiseGroupMember> members = new HashSet<>();

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "group")
    private Set<FranchiseGroupAddress> addresses = new HashSet<>();
    
    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "group")
    private Set<FranchiseLocation> locations = new HashSet<>();

	
	// *** IMPLEMENTATIONS ****

    private final static Comparator<FranchiseGroup> comparator =
            Comparator.comparing(FranchiseGroup::getName);

    @Override
    public int compareTo(final FranchiseGroup other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(
        associateWith = "organization", 
        describedAs = "Update which Organization this Franchisee belongs to")
    public FranchiseGroup updateOrganization(Organization organization) {
        setOrganization(organization);
        return this;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }
    

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
    public FranchiseGroup removeMember(
            @ParameterLayout(named = "User") final OrganizationUser user) {
        members.removeIf(m -> {
            return m.getUserName().equals(user.getUsername());
        });
        return this;
    }
    @MemberSupport
    public List<String> choices0RemoveMember() {
        return this.getMembers().stream().map(m -> m.getUserName()).collect(Collectors.toList());
    }
    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "addresses", promptStyle = PromptStyle.DIALOG)
    public FranchiseGroupAddress addAddress(
            @ParameterLayout(named = "Address Type") final FranchiseGroupAddressType addressType) {
        FranchiseGroupAddress address = FranchiseGroupAddress.withRequiredFields(this, addressType);
        addresses.add(address);
        return address;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "locations", promptStyle = PromptStyle.DIALOG)
    public FranchiseLocation createLocation(final String name) {
        FranchiseLocation location = this.franchiseLocations.create(this, name);
        locations.add(location);
        return location;
    }
}

