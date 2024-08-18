package net.savantly.nexus.flow.dom.destination;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.Programmatic;
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
import net.savantly.nexus.flow.dom.form.Form;
import net.savantly.nexus.organizations.api.HasOrganization;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".Destination")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED)
@DomainObjectLayout(cssClassFa = "circle-dot")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Destination implements Comparable<Destination>, HasOrganization {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Destination withName(Form form, DestinationType destinationType, String destinationId, String name,
            String collectionName) {
        val entity = new Destination();
        entity.id = UUID.randomUUID().toString();
        entity.setName(name);
        entity.setDestinationType(destinationType);
        entity.setDestinationId(destinationId);
        entity.setCollectionName(collectionName);
        entity.setForm(form);
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
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private String name;

    @JoinColumn(name = "org_id", nullable = false)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "name", sequence = "1.1", hidden = Where.PARENTED_TABLES)
    @Getter
    @Setter
    private Form form;

    @Column(name = "destination_type", nullable = false)
    @PropertyLayout(fieldSetId = "name", sequence = "1.5")
    @Getter
    @Setter
    private DestinationType destinationType;

    @Column(name = "destination_id", nullable = false)
    @PropertyLayout(fieldSetId = "name", sequence = "1.6")
    @Getter
    @Setter
    private String destinationId;

    @Column(name = "collection_name", nullable = false)
    @PropertyLayout(fieldSetId = "name", sequence = "1.7")
    @Getter
    @Setter
    private String collectionName;

    @Column(name = "transform_script", columnDefinition = "text", nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "1.8", multiLine = 20, hidden = Where.ALL_TABLES)
    @Getter
    @Setter
    private String transformScript;

    // *** IMPLEMENTATIONS ****

    private final static Comparator<Destination> comparator = Comparator.comparing(Destination::getName);

    @Override
    public int compareTo(final Destination other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "transformScript", describedAs = "Update script")
    public Destination updateTransformScript(@ParameterLayout(multiLine = 20) String transformScript) {
        setTransformScript(transformScript);
        return this;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(associateWith = "transformScript", describedAs = "Clear the transform script")
    public Destination clearTransformScript() {
        setTransformScript(null);
        return this;
    }

    @MemberSupport
    public String default0UpdateTransformScript() {
        return getTransformScript();
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "form", describedAs = "Update which Form this belongs to")
    public Destination updateForm(Form form) {
        setForm(form);
        return this;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    @Programmatic
    @Transient
    @Override
    public Organization getOrganization() {
        if (form != null) {
            return form.getOrganization();
        }
        return null;
    }
}
