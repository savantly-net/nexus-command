package net.savantly.franchise.dom.location;

import static org.apache.causeway.applib.annotation.SemanticsOf.IDEMPOTENT;
import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.group.FranchiseGroupRepository;
import net.savantly.franchise.dom.market.FranchiseMarket;
import net.savantly.franchise.types.EmailAddress;
import net.savantly.franchise.types.Name;
import net.savantly.franchise.types.Notes;
import net.savantly.franchise.types.PhoneNumber;

@Named(FranchiseModule.NAMESPACE + ".FranchiseLocation")
@javax.persistence.Entity
@javax.persistence.Table(
	schema=FranchiseModule.SCHEMA,
    name="franchise_location",
    uniqueConstraints = {
        @javax.persistence.UniqueConstraint(name = "franchise_location__name__UNQ", columnNames = {"NAME"})
    }
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "location-dot")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FranchiseLocation implements Comparable<FranchiseLocation>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    @Inject @Transient FranchiseGroupRepository groupRepository;
    
    public static FranchiseLocation withRequiredFields(String name) {
        val entity = new FranchiseLocation();
        entity.setName(name);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @Property(editing = Editing.DISABLED)
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;
    
    @Title
    @Name
    @Column(name="name", length = Name.MAX_LEN, nullable = false)
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


	@JoinColumn(name = "marketId", referencedColumnName = "id")
	@Getter @Setter
    @PropertyLayout(fieldSetId = "details", sequence = "1", hidden = Where.STANDALONE_TABLES)
	private FranchiseMarket market;

    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "parent")
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

