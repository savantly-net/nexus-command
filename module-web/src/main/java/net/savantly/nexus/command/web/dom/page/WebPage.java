package net.savantly.nexus.command.web.dom.page;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.command.web.NexusCommandWebModule;
import net.savantly.nexus.command.web.dom.block.Block;
import net.savantly.nexus.command.web.dom.blockType.BlockType;
import net.savantly.nexus.command.web.dom.og.OpenGraphData;
import net.savantly.nexus.command.web.dom.pageBlock.PageBlock;
import net.savantly.nexus.command.web.dom.site.WebSite;

@Named(NexusCommandWebModule.NAMESPACE + ".WebPage")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
    name = "web_page",
    schema = NexusCommandWebModule.SCHEMA
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "file-alt")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class WebPage implements Comparable<WebPage>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;

    
    public static WebPage withRequiredFields(final String name, final WebSite webSite) {
        val id = String.format("%s-%s-%s", webSite.getId(), name, UUID.randomUUID().toString().substring(0, 4));
        return withRequiredFields(name, webSite, id);
    }
    public static WebPage withRequiredFields(final String name, final WebSite webSite, final String id) {
        val entity = new WebPage();
        entity.id = id;
        entity.setName(name);
        entity.setWebSite(webSite);
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

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.0")
    @JoinColumn(name = "website_id", nullable = false)
    @Getter @Setter
    private WebSite webSite;

    @Title
    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.1")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "2")
    @Column(name = "publish_date", nullable = true)
    @Getter @Setter
    private ZonedDateTime publishDate = ZonedDateTime.now();


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "3")
    @Getter @Setter
    @Embedded
    private OpenGraphData openGraphData = new OpenGraphData();


    @Property
    @Collection
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "webPage")
    private Set<PageBlock> pageBlocks = new HashSet<>();


    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Create from Existing Block", associateWith = "pageBlocks")
    public WebPage createPageBlockFromBlock(final Block block) {
        val pageBlock = PageBlock.withRequiredFields(this, block);
        getPageBlocks().add(pageBlock);
        return this;
    }
    @MemberSupport
    public List<Block> choices0CreatePageBlockFromBlock() {
        return repositoryService.allInstances(Block.class);
    }


    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Create Block", associateWith = "pageBlocks")
    public WebPage createPageBlock(final BlockType blockType, final String name) {
        val block = this.repositoryService.persistAndFlush(Block.withRequiredFields(name, blockType));
        val pageBlock = PageBlock.withRequiredFields(this, block);
        getPageBlocks().add(pageBlock);
        return this;
    }
    @MemberSupport
    public List<BlockType> choices0CreatePageBlock() {
        return getBlockTypes();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(named = "Remove Block", associateWith = "pageBlocks")
    public WebPage removePageBlock(final PageBlock pageBlock) {
        getPageBlocks().remove(pageBlock);
        return this;
    }
    @MemberSupport
    public Set<PageBlock> choices0RemovePageBlock() {
        return getPageBlocks();
    }


    @Programmatic
    @Transient
    private List<BlockType> getBlockTypes() {
        return repositoryService.allInstances(BlockType.class);
    }

	// *** IMPLEMENTATIONS ****

    @Programmatic
    public WebPageDto toDto() {
        val dto = new WebPageDto();
        dto.setId(getId());
        dto.setName(getName());
        dto.setPublishDate(getPublishDate());
        dto.setOpenGraphData(getOpenGraphData());
        return dto;
    }

    private final static Comparator<WebPage> comparator =
            Comparator.comparing(WebPage::getId);

    @Override
    public int compareTo(final WebPage other) {
        return comparator.compare(this, other);
    }

    
}
