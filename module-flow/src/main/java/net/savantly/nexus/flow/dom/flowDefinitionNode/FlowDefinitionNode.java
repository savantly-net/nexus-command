package net.savantly.nexus.flow.dom.flowDefinitionNode;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.HashMap;
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
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import net.savantly.nexus.flow.dto.FlowNodeDto;

@Named(FlowModule.NAMESPACE + ".FlowDefinitionNode")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "form")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FlowDefinitionNode extends AuditedEntity implements Comparable<FlowDefinitionNode> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static FlowDefinitionNode withId(FlowDefinition flowDefinition, String nodeId) {
        val entity = new FlowDefinitionNode();
        entity.id = UUID.randomUUID().toString();
        entity.nodeId = nodeId;
        entity.setFlowDefinition(flowDefinition);
        return entity;
    }

    public static FlowDefinitionNode fromDto(FlowDefinition flowDefinition, FlowNodeDto dto) {
        val entity = new FlowDefinitionNode();
        entity.id = UUID.randomUUID().toString();
        entity.nodeId = dto.getId();
        entity.setFlowDefinition(flowDefinition);
        entity.setNodeData(serialize(dto.getData()));
        entity.setNodeType(dto.getType());
        entity.setNodePosition(serialize(dto.getPosition()));
        entity.setNodeStyle(serialize(dto.getStyle()));
        entity.setWidth(dto.getWidth());
        entity.setHeight(dto.getHeight());
        return entity;
    }

    private static String serialize(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @PropertyLayout(fieldSetId = "identity", sequence = "1", hidden = Where.NOWHERE)
    @Getter
    private String id;

    @Title(prepend = "Node ID: ")
    @Column(name = "node_id", nullable = false)
    @Getter
    private String nodeId;

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

    @Column(name = "node_data", columnDefinition = "text", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6", multiLine = 20)
    @Getter
    @Setter
    private String nodeData = "{}";

    @Title(prepend = "Type: ")
    @Column(name = "node_type", columnDefinition = "text", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String nodeType = "default";

    @Column(name = "node_position", columnDefinition = "text", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String nodePosition;

    @Column(name = "node_style", columnDefinition = "text", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String nodeStyle ;

    @Column(name = "width", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private int width ;

    @Column(name = "height", nullable = false)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private int height ;



    // *** IMPLEMENTATIONS ****

    private final static Comparator<FlowDefinitionNode> comparator = Comparator
            .comparing(FlowDefinitionNode::getNodeId);

    @Override
    public int compareTo(final FlowDefinitionNode other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "flowDefinition", describedAs = "Update which FlowDefinition this belongs to")
    public FlowDefinitionNode updateFlowDefinition(FlowDefinition flowDefinition) {
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
    public FlowNodeDto toDto() {

        var stringMapTypeRef = new TypeReference<HashMap<String, Object>>() {
        };

        FlowNodeDto dto = new FlowNodeDto();
        dto.setId(this.getNodeId());
        dto.setData(deserialize(this.getNodeData(), stringMapTypeRef));
        dto.setType(this.getNodeType());
        dto.setPosition(deserialize(this.getNodePosition(), stringMapTypeRef));
        dto.setStyle(deserialize(this.getNodeStyle(), stringMapTypeRef));
        dto.setWidth(this.getWidth());
        dto.setHeight(this.getHeight());
        return dto;
    }

    private <T> T deserialize(String json, TypeReference<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
