package net.savantly.nexus.command.web.dom.block;

import java.time.ZonedDateTime;
import java.util.Comparator;

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
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.dom.blockType.BlockType;

@Named(NexusCommandWebModule.NAMESPACE + ".Block")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
    name = "block",
    schema = NexusCommandWebModule.SCHEMA
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "file-alt")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Block implements Comparable<Block>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static Block withRequiredFields(final String name, final BlockType blockType) {
        val id = String.format("%s-%s", name.toLowerCase(), java.util.UUID.randomUUID().toString().substring(0, 4));
        return withRequiredFields(id, name, blockType);
    }

    public static Block withRequiredFields(final String id, final String name, final BlockType blockType) {
        val entity = new Block();
        entity.id = id;
        entity.setName(name);
        entity.setBlockType(blockType);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "3")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @JoinColumn(name = "block_type_id", nullable = false)
    @Getter @Setter
    private BlockType blockType;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "6")
    @Column(name = "publish_date", nullable = true)
    @Getter @Setter
    private ZonedDateTime publishDate = ZonedDateTime.now();

    @Property
    @PropertyLayout(multiLine = 20, fieldSetId = "content", sequence = "7")
    @Column(name = "content", nullable = true, length = 100000)
    @Getter @Setter
    private String content;

	// *** IMPLEMENTATIONS ****

    private final static Comparator<Block> comparator =
            Comparator.comparing(Block::getId);

    @Override
    public int compareTo(final Block other) {
        return comparator.compare(this, other);
    }

    
}
