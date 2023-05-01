package net.savantly.franchise.dom.web.pageTemplateContent;


import java.time.ZonedDateTime;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.applib.value.Clob;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.franchise.FranchiseModule;
import net.savantly.franchise.dom.location.FranchiseLocation;
import net.savantly.franchise.dom.web.og.OpenGraphData;
import net.savantly.franchise.dom.web.pageTemplate.WebPageTemplate;

@Named(FranchiseModule.NAMESPACE + ".WebPageTemplateContent")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "web_page_template_content"
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "code")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class WebPageTemplateContent implements Comparable<WebPageTemplateContent>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static WebPageTemplateContent withRequiredFields(final String name, final WebPageTemplate template) {
        val entity = new WebPageTemplateContent();
        entity.setName(name);
        entity.setTemplate(template);
        entity.setSlug(name.toLowerCase().replace(" ", "-"));
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
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "2")
    @Column(name = "slug", nullable = false)
    @Getter @Setter
    private String slug;

    @Property
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "3")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "4")
    @Getter @Setter
    @Embedded
    private OpenGraphData openGraphData = new OpenGraphData();

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @JoinColumn(name = "web_page_template_id", nullable = false)
    @Getter @Setter
    private WebPageTemplate template;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5.1")
    @JoinColumn(name = "location_id", nullable = true)
    @Getter @Setter
    private FranchiseLocation location;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "6")
    @Column(name = "publish_date", nullable = true)
    @Getter @Setter
    private ZonedDateTime publishDate;

    @Property
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "7")
    @Column(name = "content", nullable = true)
    @Getter @Setter
    private Clob content;

	// *** IMPLEMENTATIONS ****

    private final static Comparator<WebPageTemplateContent> comparator =
            Comparator.comparing(WebPageTemplateContent::getId);

    @Override
    public int compareTo(final WebPageTemplateContent other) {
        return comparator.compare(this, other);
    }

    
}
