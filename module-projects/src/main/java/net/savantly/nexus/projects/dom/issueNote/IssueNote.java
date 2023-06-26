package net.savantly.nexus.projects.dom.issueNote;


import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
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
import net.savantly.nexus.common.types.Notes;
import net.savantly.nexus.projects.ProjectsModule;
import net.savantly.nexus.projects.dom.issue.Issue;

@Named(ProjectsModule.NAMESPACE + ".IssueNote")
@javax.persistence.Entity
@javax.persistence.Table(
	schema=ProjectsModule.SCHEMA
)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "text")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class IssueNote implements Comparable<IssueNote>  {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static IssueNote withRequiredFields(String id, String notes, Issue issue) {
        val entity = new IssueNote();
        entity.id = id;
        entity.setNotes(notes);
        return entity;
    }

    public static IssueNote withNotes(String notes, Issue issue) {
        val id = UUID.randomUUID().toString();
        return withRequiredFields(id, notes, issue);
    }

    // *** PROPERTIES ***
    
    @Id
    @Column(name = "id", nullable = false)
    @Getter
    private String id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    public String title() {
        // return the first 30 chars of the notes
        return getNotes().substring(0, Math.min(getNotes().length(), 30));
    }

    @JoinColumn(name="issue_id")
    @Getter @Setter @ToString.Include
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    private Issue issue;

    @Notes
    @Column(length = Notes.MAX_LEN, nullable = true)
    @PropertyLayout(fieldSetId = "name", sequence = "3")
    @Getter @Setter
	private String notes;

	
	// *** IMPLEMENTATIONS ****

    private final static Comparator<IssueNote> comparator =
            Comparator.comparing(IssueNote::getNotes);

    @Override
    public int compareTo(final IssueNote other) {
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

}

