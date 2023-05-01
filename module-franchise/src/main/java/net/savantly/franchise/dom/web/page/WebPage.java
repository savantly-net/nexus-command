package net.savantly.franchise.dom.web.page;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.dom.web.block.Block;
import net.savantly.franchise.dom.web.blockType.BlockType;
import net.savantly.franchise.dom.web.og.OpenGraphData;
import net.savantly.franchise.dom.web.pageBlock.PageBlock;
import net.savantly.franchise.dom.web.site.WebSite;

@Named(FranchiseModule.NAMESPACE + ".WebPage")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "web_page"
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
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
        val entity = new WebPage();
        entity.setName(name);
        entity.setWebSite(webSite);
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

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.0")
    @JoinColumn(name = "web_site_id", nullable = false)
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
    public List<BlockType> choices0CreatePageBlock() {
        return getBlockTypes();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT)
    @ActionLayout(named = "Create Block (location specific)", associateWith = "pageBlocks")
    public WebPage createPageBlockLocationSpecific(final FranchiseLocation location, final BlockType blockType, final String name) {
        val block = this.repositoryService.persistAndFlush(Block.withRequiredFields(name, blockType, location));
        val pageBlock = PageBlock.withRequiredFields(this, block);
        getPageBlocks().add(pageBlock);
        return this;
    }
    @MemberSupport
    public List<FranchiseLocation> choices0CreatePageBlockLocationSpecific() {
        return repositoryService.allInstances(FranchiseLocation.class);
    }
    @MemberSupport
    public List<BlockType> choices1CreatePageBlockLocationSpecific() {
        return getBlockTypes();
    }

    @Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(named = "Remove Block", associateWith = "pageBlocks")
    public WebPage removePageBlock(final PageBlock pageBlock) {
        getPageBlocks().remove(pageBlock);
        return this;
    }
    public Set<PageBlock> choices0RemovePageBlock() {
        return getPageBlocks();
    }


    @Programmatic
    @Transient
    private List<BlockType> getBlockTypes() {
        return repositoryService.allInstances(BlockType.class);
    }

	// *** IMPLEMENTATIONS ****

    private final static Comparator<WebPage> comparator =
            Comparator.comparing(WebPage::getId);

    @Override
    public int compareTo(final WebPage other) {
        return comparator.compare(this, other);
    }

    
}
