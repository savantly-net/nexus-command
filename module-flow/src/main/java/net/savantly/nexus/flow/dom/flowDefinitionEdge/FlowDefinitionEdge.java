package net.savantly.nexus.flow.dom.flowDefinitionEdge;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Programmatic;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
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
import net.savantly.nexus.audited.dom.AuditedEntity;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.flow.dom.flowDefinition.FlowDefinition;
import net.savantly.nexus.flow.dto.FlowEdgeDto;

@Named(FlowModule.NAMESPACE + ".FlowDefinitionEdge")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "form")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FlowDefinitionEdge extends AuditedEntity implements Comparable<FlowDefinitionEdge> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static FlowDefinitionEdge withId(FlowDefinition flowDefinition, String edgeId) {
        val entity = new FlowDefinitionEdge();
        entity.id = UUID.randomUUID().toString();
        entity.edgeId = edgeId;
        entity.setFlowDefinition(flowDefinition);
        return entity;
    }

    public static FlowDefinitionEdge fromDto(FlowDefinition flowDefinition, FlowEdgeDto dto) {
        val entity = new FlowDefinitionEdge();
        entity.id = UUID.randomUUID().toString();
        entity.edgeId = dto.getId();
        entity.setFlowDefinition(flowDefinition);
        entity.sourceId = dto.getSource();
        entity.sourceHandleId = dto.getSourceHandle();
        entity.targetId = dto.getTarget();
        entity.targetHandleId = dto.getTargetHandle();
        entity.animated = dto.isAnimated();
        return entity;
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;
    
    @Column(name = "edge_id", nullable = false)
    @Getter
    private String edgeId;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @JoinColumn(name = "org_id", nullable = false)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.1", hidden = Where.PARENTED_TABLES)
    @Getter
    @Setter
    private FlowDefinition flowDefinition;

    @Column(name = "source_id", columnDefinition = "text", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String sourceId;

    @Column(name = "source_handle_id", columnDefinition = "text", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String sourceHandleId;

    @Column(name = "target_id", columnDefinition = "text", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String targetId;

    @Column(name = "target_handle_id", columnDefinition = "text", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String targetHandleId;

    @Column(name = "animated", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private boolean animated;


    // *** IMPLEMENTATIONS ****

    private final static Comparator<FlowDefinitionEdge> comparator = Comparator.comparing(FlowDefinitionEdge::getEdgeId);

    @Override
    public int compareTo(final FlowDefinitionEdge other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "flowDefinition", describedAs = "Update which FlowDefinition this belongs to")
    public FlowDefinitionEdge updateFlowDefinition(FlowDefinition flowDefinition) {
        setFlowDefinition(flowDefinition);
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
    public FlowEdgeDto toDto() {
        return new FlowEdgeDto()
            .setId(getEdgeId())
            .setSource(getSourceId())
            .setSourceHandle(getSourceHandleId())
            .setTarget(getTargetId())
            .setTargetHandle(getTargetHandleId())
            .setAnimated(isAnimated());
    }
}
