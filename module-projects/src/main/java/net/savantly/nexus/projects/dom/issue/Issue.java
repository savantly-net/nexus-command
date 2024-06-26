package net.savantly.nexus.projects.dom.issue;

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
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Navigable;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
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
import net.savantly.nexus.organizations.dom.organizationUser.OrganizationUser;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.IssueMember.IssueMember;
import net.savantly.nexus.projects.dom.issueNote.IssueNote;
import net.savantly.nexus.projects.dom.project.Project;

@Named(ProjectsModule.NAMESPACE + ".Issue")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = ProjectsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "note-sticky")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Issue implements Comparable<Issue> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Issue withRequiredFields(String id, String name, Project project) {
        val entity = new Issue();
        entity.id = id;
        entity.setName(name);
        entity.setProject(project);
        return entity;
    }

    public static Issue withRequiredFields(String name, Project project) {
        val id = UUID.randomUUID().toString();
        return withRequiredFields(id, name, project);
    }

    // *** PROPERTIES ***

    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter
    @Setter
    private long version;

    @Title(prepend = "Issue: ")
    @Name
    @Column(name = "NAME", length = Name.MAX_LEN, nullable = false)
    @Property
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    private String name;

    @JoinColumn(name = "project_id")
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "2", navigable = Navigable.PARENT, hidden = Where.ALL_TABLES)
    private Project project;

    @Column(length = 1000, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "3")
    @Getter
    @Setter
    private String description;

    @Column(length = 500, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "4")
    @Getter
    @Setter
    private String labels;

    @Column(name="issue_order", nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "4", named = "Order")
    @Getter
    @Setter
    private int issueOrder;

    @Collection
    @CollectionLayout(sequence = "1")
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "issue")
    @PropertyLayout(fieldSetId = "notes", sequence = "1", multiLine = 5)
    private Set<IssueNote> notes = new HashSet<>();

    @Collection
    @CollectionLayout(sequence = "2")
    @Getter
    @Setter
    @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "issue")
    private Set<IssueMember> members = new HashSet<>();

    // *** IMPLEMENTATIONS ****

    private final static Comparator<Issue> comparator = Comparator.comparing(Issue::getName);

    @Override
    public int compareTo(final Issue other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
    public Issue removeMember(
            @ParameterLayout(named = "User") final OrganizationUser user) {
        members.removeIf(m -> {
            return m.getUserName().equals(user.getUsername());
        });
        return this;
    }

    @MemberSupport
    public List<String> choices0RemoveMember() {
        return this.getMembers().stream().map(m -> m.getUserName()).collect(Collectors.toList());
    }

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "notes", promptStyle = PromptStyle.DIALOG)
    public Issue addNote(
            @ParameterLayout(named = "Note", multiLine = 5) final String note) {
        val newNote = IssueNote.withRequiredFields(UUID.randomUUID().toString(), note, this);
        this.notes.add(newNote);
        return this;
    }

}
