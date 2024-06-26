package net.savantly.nexus.franchise.dom.location;

import static org.apache.causeway.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import net.savantly.nexus.audited.api.AuditedEntity;
import net.savantly.nexus.common.types.EmailAddress;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.common.types.Notes;
import net.savantly.nexus.common.types.PhoneNumber;
import net.savantly.nexus.franchise.FranchiseModule;
import net.savantly.nexus.franchise.dom.group.FranchiseGroup;
import net.savantly.nexus.franchise.dom.group.FranchiseGroupRepository;
import net.savantly.nexus.franchise.dom.market.FranchiseMarket;

@Named(FranchiseModule.NAMESPACE + ".FranchiseLocation")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
	schema=FranchiseModule.SCHEMA,
    name="franchise_location",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "franchise_location__name__UNQ", columnNames = {"NAME"})
    }
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "location-dot")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FranchiseLocation extends AuditedEntity implements Comparable<FranchiseLocation>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    @Inject @Transient FranchiseGroupRepository groupRepository;
    
    public static FranchiseLocation withRequiredFields(String name) {
        val entity = new FranchiseLocation();
        entity.setName(name);
        return entity;
    }


    public static FranchiseLocation withRequiredFields(FranchiseGroup group, String name) {
        val entity = new FranchiseLocation();
        entity.setName(name);
        entity.setGroup(group);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Property(editing = Editing.DISABLED)
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
    @Column(name="NAME", length = Name.MAX_LEN, nullable = false)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1.1")
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
    @PropertyLayout(fieldSetId = "notes", sequence = "1", hidden = Where.STANDALONE_TABLES)
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
    @PropertyLayout(fieldSetId = "address", sequence = "6", hidden = Where.STANDALONE_TABLES)
    @Getter @Setter @ToString.Include
	private String county;

    @Column(length = 100)
    @PropertyLayout(fieldSetId = "address", sequence = "7")
    @Getter @Setter @ToString.Include
	private String country;
    
    @Column(scale = 13, precision = 16)
    @PropertyLayout(fieldSetId = "address", sequence = "8", hidden = Where.STANDALONE_TABLES)
    @Getter @Setter
	private BigDecimal lat;

    @Column(scale = 13, precision = 16)
    @PropertyLayout(fieldSetId = "address", sequence = "9", hidden = Where.STANDALONE_TABLES)
    @Getter @Setter
	private BigDecimal lon;
    


    @Column(name = "dateOpened", nullable = true)
    @Getter @Setter
    @PropertyLayout(fieldSetId = "dates", sequence = "1", hidden = Where.STANDALONE_TABLES)
	private LocalDate dateOpened;

    @Column(name = "dateClosed", nullable = true)
    @Getter @Setter
    @PropertyLayout(fieldSetId = "dates", sequence = "2", hidden = Where.STANDALONE_TABLES)
	private LocalDate dateClosed;



    @Getter @Setter
    @PropertyLayout(fieldSetId = "building", sequence = "1", hidden = Where.STANDALONE_TABLES)
	private int totalSquareFeet;
    
    @Getter @Setter
    @PropertyLayout(fieldSetId = "building", sequence = "2", hidden = Where.STANDALONE_TABLES)
	private int fohSquareFeet;

    @Getter @Setter
    @PropertyLayout(fieldSetId = "building", sequence = "3", hidden = Where.STANDALONE_TABLES)
	private int bohSquareFeet;

    @Getter @Setter
    @PropertyLayout(fieldSetId = "building", sequence = "4", hidden = Where.STANDALONE_TABLES)
	private int maxOccupancy;

    @Getter @Setter
    @PropertyLayout(fieldSetId = "building", sequence = "5", hidden = Where.STANDALONE_TABLES)
	private int maxSeating;

    
    // *** RELATIONS ***

	@JoinColumn(name = "franchise_group_id", referencedColumnName = "id", nullable = true)
	@Getter @Setter
    @PropertyLayout(fieldSetId = "name", sequence = "1")
	private FranchiseGroup group;

	@JoinColumn(name = "marketId", referencedColumnName = "id")
	@Getter @Setter
    @PropertyLayout(fieldSetId = "details", sequence = "1", hidden = Where.STANDALONE_TABLES)
	private FranchiseMarket market;

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "location")
    private Set<FranchiseLocationMember> members = new HashSet<>();

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "location")
    private Set<FranchiseLocationEmailAddress> emailAddresses = new HashSet<>();
	
	// *** IMPLEMENTATIONS ****

    private final static Comparator<FranchiseLocation> comparator =
            Comparator.comparing(FranchiseLocation::getName);

    @Override
    public int compareTo(final FranchiseLocation other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***
    
    @Action(semantics = IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "name", promptStyle = PromptStyle.INLINE)
    public FranchiseLocation updateName(
            @Name final String name) {
        setName(name);
        return this;
    }
    public String default0UpdateName() {
        return getName();
    }
    
    @MemberSupport
    public String validate0UpdateName(String newName) {
        for (char prohibitedCharacter : "&%$!".toCharArray()) {
            if( newName.contains(""+prohibitedCharacter)) {
                return "Character '" + prohibitedCharacter + "' is not allowed.";
            }
        }
        return null;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
    public FranchiseLocation removeMember(
            @ParameterLayout(named = "User") final FranchiseLocationMember user) {
        var found = members.stream().filter(m -> {
            return user.equals(m);
        }).findFirst();
        found.ifPresent(m -> m.delete());
        return this;
    }
    @MemberSupport
    public List<FranchiseLocationMember> choices0RemoveMember() {
        return this.members.stream().collect(Collectors.toList());
    }
    

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "emailAddresses", promptStyle = PromptStyle.DIALOG)
    public FranchiseLocation addEmailAddress(
            @ParameterLayout(named = "Type") final FranchiseLocationEmailAddressType addressType,
            @ParameterLayout(named = "Email Address") final String emailAddress) {
        emailAddresses.add(FranchiseLocationEmailAddress.withRequiredFields(this, addressType, emailAddress));
        return this;
    }

}

