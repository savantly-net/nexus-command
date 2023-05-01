package net.savantly.franchise.dom.web.block;

import java.time.ZonedDateTime;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.dom.web.blockType.BlockType;

@Named(FranchiseModule.NAMESPACE + ".Block")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "block"
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
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
        return withRequiredFields(name, blockType, null);
    }

    public static Block withRequiredFields(final String name, final BlockType blockType, FranchiseLocation location) {
        val entity = new Block();
        entity.setName(name);
        entity.setBlockType(blockType);
        entity.setLocation(location);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
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
    @PropertyLayout(fieldSetId = "content", sequence = "5.1")
    @JoinColumn(name = "location_id", nullable = true)
    @Getter @Setter
    private FranchiseLocation location;

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
