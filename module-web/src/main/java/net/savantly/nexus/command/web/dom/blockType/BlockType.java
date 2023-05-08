package net.savantly.nexus.command.web.dom.blockType;


import java.time.ZonedDateTime;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
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
import net.savantly.nexus.command.web.NexusCommandWebModule;

@Named(NexusCommandWebModule.NAMESPACE + ".BlockType")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "block_type",
    schema = NexusCommandWebModule.SCHEMA
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "code", describedAs = "A block type defines the schema for a block")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class BlockType implements Comparable<BlockType>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static BlockType withRequiredFields(
        final String id,
        final String name) {
        val entity = new BlockType();
        entity.id = id;
        entity.setName(name);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.1")
    @Column(name = "description", nullable = true)
    @Getter @Setter
    private String description;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "2")
    @Column(name = "publish_date", nullable = true)
    @Getter @Setter
    private ZonedDateTime publishDate = ZonedDateTime.now();

    @Property
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "3")
    @Column(name = "schema", nullable = true, length = 100000)
    @Getter @Setter
    private String schema;


    @Property
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "4")
    @Column(name = "ui_schema", nullable = true, length = 100000)
    @Getter @Setter
    private String uiSchema;

	// *** IMPLEMENTATIONS ****

    private final static Comparator<BlockType> comparator =
            Comparator.comparing(BlockType::getId);

    @Override
    public int compareTo(final BlockType other) {
        return comparator.compare(this, other);
    }

}
