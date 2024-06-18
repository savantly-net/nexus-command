package net.savantly.nexus.flow.dom.form;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
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
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dom.destinations.Destination;
import net.savantly.nexus.flow.dom.formMapping.FormMapping;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".Form")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "form")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Form implements Comparable<Form> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Form withName(Organization organization, String name) {
        val entity = new Form();
        entity.id = UUID.randomUUID().toString();
        entity.setName(name);
        entity.setOrganization(organization);
        return entity;
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
    @Name
    @Column(length = Name.MAX_LEN, nullable = false)
    @Property(editing = Editing.ENABLED)
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    private String name;

    @JoinColumn(name = "org_id", nullable = false)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.1")
    @Getter
    @Setter
    private Organization organization;

    @Column(columnDefinition = "text", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5", multiLine = 10)
    @Getter
    @Setter
    private String sampleData;



    @Column(nullable = false)
    @PropertyLayout(fieldSetId = "access", sequence = "1.2")
    @Property(editing = Editing.ENABLED)
    @Getter
    @Setter
    private boolean publicForm;
    
    @Column(nullable = true)
    @PropertyLayout(fieldSetId = "access", sequence = "1.3")
    @Getter
    @Setter
    private String apiKey;

    @Column(name = "recaptcha_enabled", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "recaptcha", sequence = "2")
    @Getter
    @Setter
    private boolean recaptchaEnabled;



    @Column(name = "recaptcha_secret", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "recaptcha", sequence = "2.3")
    @Getter
    @Setter
    private String recaptchaSecret;

    @Column(name = "recaptcha_action", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "recaptcha", sequence = "2.4")
    @Getter
    @Setter
    private String recaptchaAction;

    @Column(name = "recaptcha_threshold", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "recaptcha", sequence = "2.5")
    @Getter
    @Setter
    private float recaptchaThreshold = 0.5f;




    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "form")
    private Set<FormMapping> mappings = new HashSet<>();

    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "form")
    private Set<Destination> destinations = new HashSet<>();


    // *** IMPLEMENTATIONS ****

    @Transient
    public Form addMapping(FormMapping mapping) {
        mappings.add(mapping);
        return this;
    }

    @Transient
    public Form addDestination(Destination destination) {
        destinations.add(destination);
        return this;
    }

    private final static Comparator<Form> comparator = Comparator.comparing(Form::getName);

    @Override
    public int compareTo(final Form other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "apiKey", describedAs = "Generate a new API key")
    public Form generateApiKey() {
        setApiKey(UUID.randomUUID().toString());
        return this;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "organization", describedAs = "Update which Organization this belongs to")
    public Form updateOrganization(Organization organization) {
        setOrganization(organization);
        return this;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }
}
