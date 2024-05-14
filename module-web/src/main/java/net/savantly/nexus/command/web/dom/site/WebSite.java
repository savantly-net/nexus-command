package net.savantly.nexus.command.web.dom.site;


import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
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
import org.apache.causeway.applib.annotation.PromptStyle;
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
import net.savantly.nexus.command.web.dom.og.OpenGraphData;
import net.savantly.nexus.command.web.dom.page.WebPage;

@Named(NexusCommandWebModule.NAMESPACE + ".WebSite")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
    name = "web_site",
    schema = NexusCommandWebModule.SCHEMA
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "cloud")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class WebSite implements Comparable<WebSite>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static WebSite withRequiredFields(final String name) {
        val id = name.toLowerCase().replaceAll(" ", "-");
        return withRequiredFields(id, name);
    }

    public static WebSite withRequiredFields(final String name, final String id) {
        val entity = new WebSite();
        entity.id = id;
        entity.setName(name);
        entity.setOpenGraphData(new OpenGraphData()
        .setTitle(name)
        .setDescription("A website about " + name)
        .setImage("https://savantly.net/img/logo.png")
        .setKeywords("savantly, nexus, oss, seed, command"));
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
    @PropertyLayout(fieldSetId = "content", sequence = "1.1")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1.2")
    @Column(name = "url", nullable = true)
    @Getter @Setter
    private String url;


    @Property(editing = Editing.DISABLED)
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
    @ActionLayout(associateWith = "openGraphData", promptStyle = PromptStyle.DIALOG_MODAL)
    public WebSite updateOpenGraphData(final String title, final String description, final String image, final String keywords) {
        setOpenGraphData(new OpenGraphData()
        .setTitle(title)
        .setDescription(description)
        .setImage(image)
        .setKeywords(keywords));
        return this;
    }
    @MemberSupport
    public String default0UpdateOpenGraphData() {
        return getOpenGraphData().getTitle();
    }
    @MemberSupport
    public String default1UpdateOpenGraphData() {
        return getOpenGraphData().getDescription();
    }
    @MemberSupport
    public String default2UpdateOpenGraphData() {
        return getOpenGraphData().getImage();
    }
    @MemberSupport
    public String default3UpdateOpenGraphData() {
        return getOpenGraphData().getKeywords();
    }
    
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
    @MemberSupport
    public Set<WebPage> choices0DeleteWebPage() {
        return webPages;
    }

	// *** IMPLEMENTATIONS ****

    @Programmatic
    public WebSiteDto toDto() {
        val dto = new WebSiteDto();
        dto.setId(getId());
        dto.setName(getName());
        dto.setUrl(getUrl());
        dto.setOpenGraphData(getOpenGraphData());
        this.webPages.stream()
                .map(WebPage::toDto)
                .forEach(dto.getWebPages()::add);
        return dto;
    }

    private final static Comparator<WebSite> comparator =
            Comparator.comparing(WebSite::getId);

    @Override
    public int compareTo(final WebSite other) {
        return comparator.compare(this, other);
    }

    
}
