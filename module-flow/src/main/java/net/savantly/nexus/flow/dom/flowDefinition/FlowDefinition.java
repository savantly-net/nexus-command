package net.savantly.nexus.flow.dom.flowDefinition;


import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Programmatic;
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
import net.savantly.nexus.flow.dom.flowDefinitionEdge.FlowDefinitionEdge;
import net.savantly.nexus.flow.dom.flowDefinitionNode.FlowDefinitionNode;
import net.savantly.nexus.flow.dto.FlowDto;
import net.savantly.nexus.flow.dto.FlowEdgeDto;
import net.savantly.nexus.flow.dto.FlowNodeDto;
import net.savantly.nexus.organizations.api.OrganizationEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".FlowDefinition")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "sitemap")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FlowDefinition extends OrganizationEntity implements Comparable<FlowDefinition> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static FlowDefinition withName(Organization organization, String name) {
        val entity = new FlowDefinition();
        entity.id = UUID.randomUUID().toString();
        entity.setName(name);
        entity.setOrganization(organization);
        return entity;
    }

    public static FlowDefinition fromDto(Organization organization, FlowDto dto) {
        val entity = new FlowDefinition();
        entity.id = UUID.randomUUID().toString();
        entity.setName(dto.getName());
        entity.setOrganization(organization);
        entity.setNodes(convertNodesFromDto(entity, dto.getNodes()));
        entity.setEdges(convertEdgesFromDto(entity, dto.getEdges()));
        return entity;
    }

    private static Set<FlowDefinitionNode> convertNodesFromDto(FlowDefinition flowDefinition, Set<FlowNodeDto> nodes) {
        Set<FlowDefinitionNode> result = new HashSet<>();
        for (FlowNodeDto node : nodes) {
            FlowDefinitionNode flowDefinitionNode = FlowDefinitionNode.fromDto(flowDefinition, node);
            result.add(flowDefinitionNode);
        }
        return result;
    }

    private static Set<FlowDefinitionEdge> convertEdgesFromDto(FlowDefinition flowDefinition, Set<FlowEdgeDto> edges) {
        Set<FlowDefinitionEdge> result = new HashSet<>();
        for (FlowEdgeDto edge : edges) {
            FlowDefinitionEdge flowDefinitionEdge = FlowDefinitionEdge.fromDto(flowDefinition, edge);
            result.add(flowDefinitionEdge);
        }
        return result;
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

    @Column(name = "public_access", nullable = false)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.2")
    @Property(editing = Editing.ENABLED)
    @Getter
    @Setter
    private boolean publicAccess;

    @Column(nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.3")
    @Getter
    @Setter
    private String apiKey;


    @Column(columnDefinition = "text", nullable = true)
    @Property(editing = Editing.ENABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5", multiLine = 10)
    @Getter
    @Setter
    private String sampleData;


    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "flowDefinition")
    @CollectionLayout(sequence = "1")
    private Set<FlowDefinitionNode> nodes = new HashSet<>();

    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "flowDefinition")
    @CollectionLayout(sequence = "2")
    private Set<FlowDefinitionEdge> edges = new HashSet<>();


    // *** IMPLEMENTATIONS ****

    private final static Comparator<FlowDefinition> comparator = Comparator.comparing(FlowDefinition::getName);

    @Override
    public int compareTo(final FlowDefinition other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "apiKey", describedAs = "Generate a new API key")
    public FlowDefinition generateApiKey() {
        setApiKey(UUID.randomUUID().toString());
        return this;
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "organization", describedAs = "Update which Organization this belongs to")
    public FlowDefinition updateOrganization(Organization organization) {
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

    @Programmatic
    public void addNode(FlowDefinitionNode node) {
        nodes.add(node);
    }

    @Programmatic
    public void addEdge(FlowDefinitionEdge edge) {
        edges.add(edge);
    }

    @Programmatic
    public FlowDto toDto() {
        FlowDto dto = new FlowDto();
        dto.setName(getName());
        dto.setNodes(convertNodesToDto());
        dto.setEdges(convertEdgesToDto());
        return dto;
    }

    private Set<FlowNodeDto> convertNodesToDto() {
        Set<FlowNodeDto> result = new HashSet<>();
        for (FlowDefinitionNode node : nodes) {
            result.add(node.toDto());
        }
        return result;
    }

    private Set<FlowEdgeDto> convertEdgesToDto() {
        Set<FlowEdgeDto> result = new HashSet<>();
        for (FlowDefinitionEdge edge : edges) {
            result.add(edge.toDto());
        }
        return result;
    }
    
}
