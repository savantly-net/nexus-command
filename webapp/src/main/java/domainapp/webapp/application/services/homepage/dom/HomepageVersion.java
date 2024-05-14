package domainapp.webapp.application.services.homepage.dom;

import java.util.Comparator;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import org.apache.causeway.valuetypes.markdown.applib.value.Markdown;

import domainapp.webapp.application.ApplicationModule;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@Named(ApplicationModule.PUBLIC_NAMESPACE + ".HomepageVersion")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
    name = "homepage_version",
    schema = ApplicationModule.PUBLIC_NAMESPACE
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "home")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class HomepageVersion implements Comparable<HomepageVersion>  {
	

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static HomepageVersion withRequiredFields() {
        val entity = new HomepageVersion();
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    @Column(name = "id", nullable = false)
    @Getter
    @Title(prepend = "Homepage Version: ")
    private Long id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "1")
    @Column(name = "name", nullable = true)
    @Getter @Setter
    private String name;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "2")
    @Column(name = "published", nullable = false)
    @Getter @Setter
    private boolean published;

    @Property
    @PropertyLayout(multiLine = 30, fieldSetId = "content", sequence = "3")
    @Column(name = "body", nullable = true)
    @Getter @Setter
    private Markdown body;

	// *** IMPLEMENTATIONS ****

    private final static Comparator<HomepageVersion> comparator =
            Comparator.comparing(HomepageVersion::getId);

    @Override
    public int compareTo(final HomepageVersion other) {
        return comparator.compare(this, other);
    }

    
}
