package net.savantly.nexus.projects.dom.persona;

import java.util.Comparator;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Id;
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
import net.savantly.ai.languagetools.HasPrompt;
import net.savantly.nexus.common.types.Name;
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
public class Persona implements Comparable<Persona>, HasPrompt {

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
        val id = idGenerator(name);
        return withRequiredFields(id, name);
    }

    public static Persona fromDto(PersonaDTO dto) {
        val entity = new Persona();
        entity.id = dto.getId();
        entity.version = dto.getVersion();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setGoals(dto.getGoals());
        entity.setPainPoints(dto.getPainPoints());
        entity.setBehaviors(dto.getBehaviors());
        entity.setTechnologyUse(dto.getTechnologyUse());
        entity.setAge(dto.getAge());
        entity.setGender(dto.getGender());
        entity.setEducationLevel(dto.getEducationLevel());
        entity.setOccupation(dto.getOccupation());
        entity.setLocation(dto.getLocation());
        entity.setIndustry(dto.getIndustry());
        entity.setJobRole(dto.getJobRole());
        entity.setCareerPath(dto.getCareerPath());
        entity.setSkills(dto.getSkills());
        entity.setPersonalityTraits(dto.getPersonalityTraits());
        entity.setPersonalValues(dto.getPersonalValues());
        entity.setMotivations(dto.getMotivations());
        entity.setFrustrations(dto.getFrustrations());

        if (dto.getId() == null) {
            entity.id = idGenerator(dto.getName());
        }
        return entity;
    }

    private static String idGenerator(String name) {
        return String.format("%s-%s", name.toLowerCase(), UUID.randomUUID().toString().substring(0, 8));
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

    @Title(prepend = "P: ")
    @Name
    @Column(name = "NAME", length = Name.MAX_LEN, nullable = false)
    @Property
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private String name;

    /****************
     * DESCRIPTION *
     ****************/

    @Column(name = "description", length = 1000, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    @Getter
    @Setter
    private String description;


    @Column(name = "goals", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    @Getter
    @Setter
    private String goals;

    @Column(name = "pain_points", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    @Getter
    @Setter
    private String painPoints;

    @Column(name = "behaviors", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    @Getter
    @Setter
    private String behaviors;

    @Column(name = "technology_use", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    @Getter
    @Setter
    private String technologyUse;

    /****************
     * DEMOGRAPHICS *
     ****************/

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "demographics", sequence = "2")
    @Column(name = "demographic_age", nullable = false)
    private int age = 40;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "demographics", sequence = "2")
    @Column(name = "demographic_gender", nullable = true)
    private String gender;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "demographics", sequence = "2")
    @Column(name = "demographic_education_level", nullable = true)
    private String educationLevel;

    @Title(prepend = " (", append = ")", sequence = "2")
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "demographics", sequence = "2")
    @Column(name = "demographic_occupation", nullable = true)
    private String occupation;

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "demographics", sequence = "2")
    @Column(name = "demographic_location", nullable = true)
    private String location;

    /****************
     * PROFESSIONAL *
     * BACKGROUND *
     * **************
     */

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "professional", sequence = "2")
    @Column(name = "professional_industry", nullable = true)
    private String industry;
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "professional", sequence = "2")
    @Column(name = "professional_job_role", nullable = true)
    private String jobRole;
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "professional", sequence = "2")
    @Column(name = "professional_career_path", nullable = true)
    private String careerPath;
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "professional", sequence = "2", multiLine = 3)
    @Column(name = "professional_skills", nullable = true, length = 500)
    private String skills;

    /*****************
     * Psychographics *
     * ****************
     */

    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "psychographics", sequence = "2", multiLine = 3)
    @Column(name = "psy_personality_traits", nullable = true, length = 500)
    private String personalityTraits;
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "psychographics", sequence = "2", multiLine = 3)
    @Column(name = "psy_personal_values", nullable = true, length = 500)
    private String personalValues;
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "psychographics", sequence = "2", multiLine = 3)
    @Column(name = "psy_motivations", nullable = true, length = 500)
    private String motivations;
    @Getter
    @Setter
    @Property
    @PropertyLayout(fieldSetId = "psychographics", sequence = "2", multiLine = 3)
    @Column(name = "psy_frustrations", nullable = true, length = 500)
    private String frustrations;


    // *** IMPLEMENTATIONS ****

    private final static Comparator<Persona> comparator = Comparator.comparing(Persona::getName);

    @Override
    public int compareTo(final Persona other) {
        return comparator.compare(this, other);
    }

    @Override
    public String getPrompt() {
        var sb = new StringBuilder();
        sb.append("Persona: ").append(getName());
        if (getDescription() != null) {
            sb.append(" - ").append(getDescription()).append("\n");
        }
        if (getGoals() != null) {
            sb.append("Goals: ").append(getGoals()).append("\n");
        }
        if (getPainPoints() != null) {
            sb.append("Pain Points: ").append(getPainPoints()).append("\n");
        }
        if (getBehaviors() != null) {
            sb.append("Behaviors: ").append(getBehaviors()).append("\n");
        }
        if (getTechnologyUse() != null) {
            sb.append("Technology Use: ").append(getTechnologyUse()).append("\n");
        }
        if (getAge() != 0) {
            sb.append("Age: ").append(getAge()).append("\n");
        }
        if (getGender() != null) {
            sb.append("Gender: ").append(getGender()).append("\n");
        }
        if (getEducationLevel() != null) {
            sb.append("Education Level: ").append(getEducationLevel()).append("\n");
        }
        if (getOccupation() != null) {
            sb.append("Occupation: ").append(getOccupation()).append("\n");
        }
        if (getLocation() != null) {
            sb.append("Location: ").append(getLocation()).append("\n");
        }
        if (getIndustry() != null) {
            sb.append("Industry: ").append(getIndustry()).append("\n");
        }
        if (getJobRole() != null) {
            sb.append("Job Role: ").append(getJobRole()).append("\n");
        }
        if (getCareerPath() != null) {
            sb.append("Career Path: ").append(getCareerPath()).append("\n");
        }
        if (getSkills() != null) {
            sb.append("Skills: ").append(getSkills()).append("\n");
        }
        if (getPersonalityTraits() != null) {
            sb.append("Personality Traits: ").append(getPersonalityTraits()).append("\n");
        }
        if (getPersonalValues() != null) {
            sb.append("Personal Values: ").append(getPersonalValues()).append("\n");
        }
        if (getMotivations() != null) {
            sb.append("Motivations: ").append(getMotivations()).append("\n");
        }
        if (getFrustrations() != null) {
            sb.append("Frustrations: ").append(getFrustrations()).append("\n");
        }
        return sb.toString();
    }

}
