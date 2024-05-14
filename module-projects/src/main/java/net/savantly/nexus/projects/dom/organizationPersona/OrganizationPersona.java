package net.savantly.nexus.projects.dom.organizationPersona;

import java.util.Comparator;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.persona.Persona;

@Named(ProjectsModule.NAMESPACE + ".OrganizationPersona")
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "org_persona", schema = ProjectsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "circle-user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class OrganizationPersona implements Comparable<OrganizationPersona> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static OrganizationPersona withRequiredFields(final Persona persona, final Organization organization) {
        val entity = new OrganizationPersona();
        entity.setPersona(persona);
        entity.setOrganization(organization);
        return entity;
    }

    // *** PROPERTIES ***

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "3")
    @JoinColumn(name = "persona_id", nullable = false)
    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Persona persona;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "4")
    @JoinColumn(name = "organization_id", nullable = false)
    @Getter
    @Setter
    private Organization organization;

    // *** IMPLEMENTATIONS ****

    @Title
    @Transient
    public String getTitle() {
        return String.format("%s - %s", titleService.titleOf(this.organization), titleService.titleOf(this.persona));
    }

    private final static Comparator<OrganizationPersona> comparator = Comparator.comparing(OrganizationPersona::getId);

    @Override
    public int compareTo(final OrganizationPersona other) {
        return comparator.compare(this, other);
    }

}
