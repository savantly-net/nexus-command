package net.savantly.nexus.projects.dom.persona;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Bounding;
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
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.projects.ProjectsModule;

@Named(ProjectsModule.NAMESPACE + ".Persona")
@javax.persistence.Entity
@javax.persistence.Table(schema = ProjectsModule.SCHEMA, name = "persona")
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Persona implements Comparable<Persona> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Persona withRequiredFields(String id, String name) {
        val entity = new Persona();
        entity.id = id;
        entity.setName(name);
        return entity;
    }

    public static Persona withName(String name) {
        val id = String.format("%s-%s", name.toLowerCase(), UUID.randomUUID().toString().substring(0, 8));
        return withRequiredFields(id, name);
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @Title(prepend = "Persona: ")
    @Name
    @Column(name = "NAME", length = Name.MAX_LEN, nullable = false)
    @Property(editing = Editing.DISABLED)
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private String name;

    @JoinColumn(name = "organization_id")
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    private Organization organization;

    @Embedded
    private DemographicInfo demographicInfo;

    @Embedded
    private ProfessionalBackground professionalBackground;

    @Embedded
    private Psychographics psychographics;

    @ElementCollection
    private List<String> goals;

    @ElementCollection
    private List<String> painPoints;

    @ElementCollection
    private List<String> behaviors;

    @ElementCollection
    private List<String> scenarios;

    @ElementCollection
    private List<String> technologyUse;

    // *** IMPLEMENTATIONS ****

    private final static Comparator<Persona> comparator = Comparator.comparing(Persona::getName);

    @Override
    public int compareTo(final Persona other) {
        return comparator.compare(this, other);
    }

}
