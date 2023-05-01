package net.savantly.franchise.dom.web.site;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.FetchType;
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
import net.savantly.franchise.dom.brand.Brand;
import net.savantly.franchise.dom.web.og.OpenGraphData;
import net.savantly.franchise.dom.web.page.WebPage;

@Named(FranchiseModule.NAMESPACE + ".WebSite")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "web_site"
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "cloud")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class WebSite implements Comparable<WebSite>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static WebSite withRequiredFields(final Brand brand, final String name) {
        val entity = new WebSite();
        entity.id = name.toLowerCase().replaceAll(" ", "-");
        entity.setName(name);
        entity.setBrand(brand);
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
    @PropertyLayout(fieldSetId = "content", sequence = "1")
    @JoinColumn(name = "brand_id", nullable = false)
    @Getter @Setter
    private Brand brand;

    @Title
    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.1")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.4", named = "Default Open Graph Data")
    @Embedded
    @Getter @Setter
    private OpenGraphData openGraphData = new OpenGraphData();

    @Collection
    @Property
    @Getter @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "webSite")
    private Set<WebPage> webPages = new HashSet<>();

    
    @Action
    @ActionLayout(associateWith = "webPages")
    public WebPage createWebPage(final String name) {
        val webPage = WebPage.withRequiredFields(name, this);
        webPages.add(webPage);
        return webPage;
    }

    @Action
    @ActionLayout(associateWith = "webPages")
    public WebSite deleteWebPage(final WebPage webPage) {
        webPages.remove(webPage);
        this.repositoryService.removeAndFlush(webPage);
        this.messageService.informUser("Deleted " + webPage.getName());
        return this;
    }
    public Set<WebPage> choices0DeleteWebPage() {
        return webPages;
    }

	// *** IMPLEMENTATIONS ****

    private final static Comparator<WebSite> comparator =
            Comparator.comparing(WebSite::getId);

    @Override
    public int compareTo(final WebSite other) {
        return comparator.compare(this, other);
    }

    
}
