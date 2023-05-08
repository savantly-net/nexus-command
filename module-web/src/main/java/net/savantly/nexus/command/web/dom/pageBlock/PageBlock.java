package net.savantly.nexus.command.web.dom.pageBlock;

import java.time.ZonedDateTime;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
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
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.dom.block.Block;
import net.savantly.nexus.command.web.dom.page.WebPage;

@Named(NexusCommandWebModule.NAMESPACE + ".PageBlock")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "page_block",
    schema = NexusCommandWebModule.SCHEMA
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "cube")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class PageBlock implements Comparable<PageBlock>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static PageBlock withRequiredFields(final WebPage webPage, final Block block) {
        val id = String.format("%s-%s", webPage.getId(), block.getId());
        return withRequiredFields(webPage, block, id);
    }
    public static PageBlock withRequiredFields(final WebPage webPage, final Block block, String id) {
        val entity = new PageBlock();
        entity.setWebPage(webPage);
        entity.setBlock(block);
        entity.id = id;
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

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "2")
    @Column(name = "publish_date", nullable = true)
    @Getter @Setter
    private ZonedDateTime publishDate = ZonedDateTime.now();

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "3")
    @JoinColumn(name = "webpage_id", nullable = false)
    @Getter @Setter
    private WebPage webPage;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "4")
    @JoinColumn(name = "block", nullable = false)
    @Getter @Setter
    private Block block;

	// *** IMPLEMENTATIONS ****

    @Title
    @Transient
    public String getTitle() {
        return String.format("%s - %s", titleService.titleOf(this.block), titleService.titleOf(this.webPage));
    }

    private final static Comparator<PageBlock> comparator =
            Comparator.comparing(PageBlock::getId);

    @Override
    public int compareTo(final PageBlock other) {
        return comparator.compare(this, other);
    }

    
}
