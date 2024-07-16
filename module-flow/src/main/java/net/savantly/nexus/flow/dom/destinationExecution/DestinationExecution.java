package net.savantly.nexus.flow.dom.destinationExecution;


import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.appfeatui.ApplicationFeatureViewModel.Parent;
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
import net.savantly.nexus.audited.api.AuditedEntity;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dom.destination.Destination;

@Named(FlowModule.NAMESPACE + ".DestinationExecution")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class DestinationExecution extends AuditedEntity implements Comparable<DestinationExecution> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static DestinationExecution withResult(Destination destination, boolean successful, String message) {
        val entity = new DestinationExecution();
        entity.id = UUID.randomUUID().toString();
        entity.destination = destination;
        entity.successful = successful;
        entity.message = message;
        return entity;
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    @Title(prepend = "Destination Execution: ")
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;


    @JoinColumn(name = "destination_id", nullable = false)
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "1.1", hidden = Where.PARENTED_TABLES)
    @Getter
    @Setter
    @Parent
    private Destination destination;

    @Column(name = "successful", nullable = false)
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "1.2")
    @Getter
    @Setter
    private boolean successful;


    @Column(columnDefinition = "text", nullable = true)
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5", multiLine = 10)
    @Getter
    @Setter
    private String message;


    // *** IMPLEMENTATIONS ****

    private final static Comparator<DestinationExecution> comparator = Comparator.comparing(DestinationExecution::getId);

    @Override
    public int compareTo(final DestinationExecution other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***
    
}
