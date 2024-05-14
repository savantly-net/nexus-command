package net.savantly.nexus.projects.dom.project;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;
import org.apache.causeway.valuetypes.markdown.applib.value.Markdown;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.ai.languagetools.HasPrompt;
import net.savantly.nexus.common.types.Description;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.issue.Issue;
import net.savantly.nexus.projects.dom.projectMember.ProjectMember;

@Named(ProjectsModule.NAMESPACE + ".Project")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
	schema=ProjectsModule.SCHEMA,
    name = "project",
    uniqueConstraints = {
        @jakarta.persistence.UniqueConstraint(name = "project__name__UNQ", columnNames = {"NAME"})
    }
)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "shapes")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Project implements Comparable<Project>, HasPrompt  {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static Project withRequiredFields(String id, String name) {
        val entity = new Project();
        entity.id = id;
        entity.setName(name);
        return entity;
    }

    public static Project withName(String name) {
        val id = String.format("%s-%s", name.toLowerCase(), UUID.randomUUID().toString().substring(0, 8));
        return withRequiredFields(id, name);
    }

    // *** PROPERTIES ***
    
    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title(prepend = "Project: ")
    @Name
    @Column(name="NAME", length = Name.MAX_LEN, nullable = false)
    @Property(editing = Editing.DISABLED)
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private String name;

    @JoinColumn(name="organization_id")
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    private Organization organization;

    @Column(length = 2000, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "3", multiLine = 10)
    @Getter @Setter
	private String description;
    
    @Action
    @ActionLayout(named = "Update Description", associateWith = "description", describedAs = "Update the project description", promptStyle = PromptStyle.DIALOG)
    public Project updateDescription(@ParameterLayout(multiLine = 5) String description) {
        this.description = description;
        return this;
    }


    @Column(nullable = true, columnDefinition = "TEXT")
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "plan", sequence = "3", multiLine = 8)
    @Getter @Setter
	private Markdown plan;

    @Action
    @ActionLayout(named = "Update Plan", associateWith = "plan", describedAs = "Update the project plan", promptStyle = PromptStyle.DIALOG)
    public Project updatePlan(String plan) {
        this.plan = Markdown.valueOf(plan);
        return this;
    }

    
    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "project")
	private Set<ProjectMember> members = new HashSet<>();


    @Collection
    @Getter @Setter
	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL}, mappedBy = "project")
	private Set<Issue> issues = new HashSet<>();

	
	// *** IMPLEMENTATIONS ****

    private final static Comparator<Project> comparator =
            Comparator.comparing(Project::getName);

    @Override
    public int compareTo(final Project other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
    public Project removeMember(
            @ParameterLayout(named = "User") final OrganizationUser user) {
        members.removeIf(m -> {
            return m.getUsername().equals(user.getUsername());
        });
        return this;
    }
    @MemberSupport
    public List<String> choices0RemoveMember() {
        return this.getMembers().stream().map(m -> m.getUsername()).collect(Collectors.toList());
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "issues", promptStyle = PromptStyle.DIALOG)
    public Issue addIssue(
            @ParameterLayout(named = "Name") final String name) {
        val issue = Issue.withRequiredFields(UUID.randomUUID().toString(), name, this);
        this.issues.add(issue);
        return repositoryService.persistAndFlush(issue);
    }

    @Override
    public String getPrompt() {
        var sb = new StringBuilder();
        sb
            .append("Project: ")
            .append(this.getName())
            .append(description != null ? " - " + description : "");
        return sb.toString();
    }

}

