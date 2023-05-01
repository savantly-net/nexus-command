package net.savantly.franchise.dom.web.pageTemplate;


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
import net.savantly.franchise.dom.web.pageTemplateType.WebPageTemplateType;

@Named(FranchiseModule.NAMESPACE + ".WebPageTemplate")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "web_page_template"
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "code")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class WebPageTemplate implements Comparable<WebPageTemplate>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static WebPageTemplate withRequiredFields(final String name) {
        val entity = new WebPageTemplate();
        entity.setName(name);
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
    @PropertyLayout(fieldSetId = "content", sequence = "1")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "2")
    @JoinColumn(name = "template_type", nullable = true)
    @Getter @Setter
    private WebPageTemplateType templateType;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "2")
    @Column(name = "publish_date", nullable = true)
    @Getter @Setter
    private ZonedDateTime publishDate;

    @Property
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "3")
    @Column(name = "template", nullable = true)
    @Getter @Setter
    private Clob template;

	// *** IMPLEMENTATIONS ****

    private final static Comparator<WebPageTemplate> comparator =
            Comparator.comparing(WebPageTemplate::getId);

    @Override
    public int compareTo(final WebPageTemplate other) {
        return comparator.compare(this, other);
    }

    
}
