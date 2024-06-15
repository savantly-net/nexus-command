package net.savantly.nexus.projects.dom.projectPersona;

import java.util.Comparator;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.ai.languagetools.HasPrompt;
import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.agents.dom.persona.Persona;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.project.Project;

@Named(ProjectsModule.NAMESPACE + ".ProjectPersona")
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "project_persona", schema = ProjectsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "circle-user")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class ProjectPersona implements Comparable<ProjectPersona>, HasPrompt {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static ProjectPersona withRequiredFields(final Persona persona, final Project project) {
        val entity = new ProjectPersona();
        entity.setPersona(persona);
        entity.setProject(project);
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
    @JoinColumn(name = "project_id", nullable = false)
    @Getter
    @Setter
    private Project project;


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "goal", columnDefinition = "text", nullable = true)
    @Getter
    @Setter
    private String goal;


    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "influence_factors", columnDefinition = "text", nullable = true)
    @Getter
    @Setter
    private String influenceFactors;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "trusted_sources", columnDefinition = "text", nullable = true)
    @Getter
    @Setter
    private String trustedInformationSources;

    // *** IMPLEMENTATIONS ****

    @Title
    @Transient
    public String getTitle() {
        return String.format("%s (%s)", this.persona.getName(), this.project.getName());
    }

    private final static Comparator<ProjectPersona> comparator = Comparator.comparing(ProjectPersona::getId);

    @Override
    public int compareTo(final ProjectPersona other) {
        return comparator.compare(this, other);
    }

    public void setDetailsFromDto(ProjectPersonaDetailsDTO personaDetailsDto) {
        this.setInfluenceFactors(personaDetailsDto.getInfluenceFactors());
        this.setTrustedInformationSources(personaDetailsDto.getTrustedInformationSources());
    }

    @Override
    public String getPrompt() {
        var basePrompt = PromptBuilder.format(this.project, this.persona);
        var sb = new StringBuilder();
        sb.append(basePrompt);
        sb.append("\n");
        if (this.influenceFactors != null) {
            sb.append("Influence Factors: ");
            sb.append(this.influenceFactors);
            sb.append("\n");
        }
        if (this.trustedInformationSources != null) {
            sb.append("Trusted Information Sources: ");
            sb.append(this.trustedInformationSources);
            sb.append("\n");
        }
        return sb.toString();

    }

}
