package net.savantly.franchise.dom.brand;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.brandAddress.BrandAddress;
import net.savantly.franchise.dom.brandAddress.BrandAddressType;
import net.savantly.franchise.dom.franchiseUser.FranchiseUser;
import net.savantly.franchise.dom.franchiseUser.FranchiseUsers;
import net.savantly.franchise.dom.group.FranchiseGroup;
import net.savantly.franchise.types.EmailAddress;
import net.savantly.franchise.types.Name;
import net.savantly.franchise.types.Notes;
import net.savantly.franchise.types.PhoneNumber;

@Named(FranchiseModule.NAMESPACE + ".Brand")
@javax.persistence.Entity
@javax.persistence.Table(
	schema=FranchiseModule.SCHEMA,
    uniqueConstraints = {
        @javax.persistence.UniqueConstraint(name = "brand__name__UNQ", columnNames = {"NAME"})
    }
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "copyright")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Brand implements Comparable<Brand>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    @Inject @Transient FranchiseUsers userRepository;
    
    public static Brand withName(String name) {
        val entity = new Brand();
        entity.setName(name);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
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
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "brand")
	private Set<BrandMember> members = new HashSet<>();

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "brand")
    private Set<BrandAddress> addresses = new HashSet<>();
    

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "brand")
    private Set<FranchiseGroup> groups = new HashSet<>();

	
	// *** IMPLEMENTATIONS ****

    private final static Comparator<Brand> comparator =
            Comparator.comparing(Brand::getName);

    @Override
    public int compareTo(final Brand other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

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
    public Brand addMember(
    		@ParameterLayout(named = "User") final FranchiseUser user, 
    		@ParameterLayout(named = "Role") final BrandMemberRole role) {
    	members.add(BrandMember.withRequiredFields(this, role, user));
        return this;
    }

    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
    public Brand removeMember(
            @ParameterLayout(named = "User") final FranchiseUser user) {
        members.removeIf(m -> {
            return m.getUserName().equals(user.getUsername());
        });
        return this;
    }
    public List<String> choices0RemoveMember() {
        return this.getMembers().stream().map(m -> m.getUserName()).collect(Collectors.toList());
    }
    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "addresses", promptStyle = PromptStyle.DIALOG)
    public BrandAddress addAddress(
            @ParameterLayout(named = "Address Type") final BrandAddressType addressType) {
        BrandAddress address = BrandAddress.withRequiredFields(this, addressType);
        addresses.add(address);
        return address;
    }


    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "groups", promptStyle = PromptStyle.DIALOG)
    public Brand addGroup(
    		@ParameterLayout(named = "Franchisee") final FranchiseGroup group) {
            groups.add(group);
            group.setBrand(this);
        return this;
    }
    public List<FranchiseGroup> choices0AddGroup() {
        return repositoryService.allInstances(FranchiseGroup.class);
    }

    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "groups", promptStyle = PromptStyle.DIALOG)
    public Brand removeGroup(
            @ParameterLayout(named = "Franchisee") final FranchiseGroup group) {
        groups.removeIf(g -> g.getId().equals(group.getId()));
        return this;
    }
    public Set<FranchiseGroup> choices0RemoveGroup() {
        return this.getGroups();
    }
}

