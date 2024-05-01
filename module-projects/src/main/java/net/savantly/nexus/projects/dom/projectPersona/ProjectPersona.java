package net.savantly.nexus.projects.dom.projectPersona;

import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import net.savantly.nexus.projects.dom.project.Project;
import net.savantly.ai.languagetools.HasPrompt;
import net.savantly.ai.languagetools.PromptBuilder;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.persona.Persona;

@Named(ProjectsModule.NAMESPACE + ".ProjectPersona")
@javax.persistence.Entity
@javax.persistence.Table(name = "project_persona", schema = ProjectsModule.SCHEMA)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
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
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "metadata", sequence = "1")
    @Column(name = "id", nullable = false)
    @Getter
    private Long id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
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
    @Column(name = "needs", nullable = true)
    @Getter
    @Setter
    private String needs;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "tasks", nullable = true)
    @Getter
    @Setter
    private String tasksToAccomplish;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "influence_factors", nullable = true)
    @Getter
    @Setter
    private String influenceFactors;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "trusted_sources", nullable = true)
    @Getter
    @Setter
    private String trustedInformationSources;

    @Property
    @PropertyLayout(fieldSetId = "content", sequence = "5")
    @Column(name = "use_cases", nullable = true)
    @Getter
    @Setter
    private String useCases;

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
        this.setNeeds(personaDetailsDto.getNeeds());
        this.setTasksToAccomplish(personaDetailsDto.getTasksToAccomplish());
        this.setInfluenceFactors(personaDetailsDto.getInfluenceFactors());
        this.setTrustedInformationSources(personaDetailsDto.getTrustedInformationSources());
        this.setUseCases(personaDetailsDto.getUseCases());
    }

    @Override
    public String getPrompt() {
        var basePrompt = PromptBuilder.format(this.project, this.persona);
        var sb = new StringBuilder();
        sb.append(basePrompt);
        sb.append("\n");
        if (this.needs != null) {
            sb.append("Needs: ");
            sb.append(this.needs);
            sb.append("\n");
        }
        if (this.tasksToAccomplish != null) {
            sb.append("Tasks to Accomplish: ");
            sb.append(this.tasksToAccomplish);
            sb.append("\n");
        }
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
        if (this.useCases != null) {
            sb.append("Use Cases: ");
            sb.append(this.useCases);
            sb.append("\n");
        }
        return sb.toString();

    }

}
