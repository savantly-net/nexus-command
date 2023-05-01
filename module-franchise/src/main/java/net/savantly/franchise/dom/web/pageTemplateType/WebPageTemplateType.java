package net.savantly.franchise.dom.web.pageTemplateType;


import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Id;
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
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.franchise.FranchiseModule;

@Named(FranchiseModule.NAMESPACE + ".WebPageTemplateType")
@javax.persistence.Entity
@javax.persistence.Table(
    name = "web_page_template_type"
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "page")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class WebPageTemplateType implements Comparable<WebPageTemplateType>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static WebPageTemplateType withRequiredFields(final String id, final String name) {
        val entity = new WebPageTemplateType();
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


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1")
    @Column(name = "name", nullable = false)
    @Getter @Setter
    private String name;

	// *** IMPLEMENTATIONS ****

    private final static Comparator<WebPageTemplateType> comparator =
            Comparator.comparing(WebPageTemplateType::getId);

    @Override
    public int compareTo(final WebPageTemplateType other) {
        return comparator.compare(this, other);
    }

    
}
