package net.savantly.nexus.gaform.dom.form;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;
import static org.apache.causeway.applib.annotation.SemanticsOf.IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
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
import net.savantly.nexus.flow.dom.form.Form;
import net.savantly.nexus.ga.GoogleAnalyticsModule;
import net.savantly.nexus.ga.dom.gaConnection.GAConnection;

@Named(GoogleAnalyticsModule.NAMESPACE + ".FormGAConection")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = GoogleAnalyticsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "link")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FormGAConnection implements Comparable<FormGAConnection> {

    @Inject
    @Transient
    RepositoryService repositoryService;

    @Inject
    @Transient
    MessageService messageService;

    public static FormGAConnection withRequiredArgs(Form form, GAConnection gaConnection) {
        val entity = new FormGAConnection();
        entity.id = UUID.randomUUID().toString();
        entity.form = form;
        entity.connection = gaConnection;
        return entity;
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @Title
    public String title() {
        return String.format("%s - %s", form.getName(), connection.getName());
    }

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @JoinColumn(name = "ga_connection_id", nullable = true)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.7")
    @Getter
    private GAConnection connection;

    @JoinColumn(name = "form_id", nullable = true)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.7")
    @Getter
    private Form form;

    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.7")
    @Getter
    @Setter
    @Column(name = "event_name", nullable = true)
    private String eventName;

    // *** IMPLEMENTATIONS ****

    private final static Comparator<FormGAConnection> comparator = Comparator.comparing(FormGAConnection::getId);

    @Override
    public int compareTo(final FormGAConnection other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        messageService.informUser(String.format("'%s' deleted", this.title()));
        repositoryService.removeAndFlush(this);
    }

    @Action(semantics = IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(describedAs = "Update the event name")
    public FormGAConnection updateEventName(final String eventName) {
        this.eventName = eventName;
        return this;
    }

    @MemberSupport
    public String default0UpdateEventName() {
        return this.eventName;
    }
}
