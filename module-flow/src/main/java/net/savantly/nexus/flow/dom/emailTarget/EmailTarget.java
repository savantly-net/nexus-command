package net.savantly.nexus.flow.dom.emailTarget;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
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

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.flow.FlowModule;
import net.savantly.nexus.organizations.api.OrganizationEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(FlowModule.NAMESPACE + ".EmailTarget")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = FlowModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "envelope")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class EmailTarget extends OrganizationEntity implements Comparable<EmailTarget> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static EmailTarget withName(Organization organization, String name) {
        val entity = new EmailTarget();
        entity.id = UUID.randomUUID().toString();
        entity.setName(name);
        entity.setOrganization(organization);
        return entity;
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

    @Title
    @Name
    @Column(length = Name.MAX_LEN, nullable = false)
    @Property(editing = Editing.DISABLED)
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    private String name;

    @Column(name="email_to", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5")
    @Getter
    @Setter
    private String to;

    @Column(name="email_cc", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    private String cc;

    @Column(name="email_bcc", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.7")
    @Getter
    @Setter
    private String bcc;

    @Column(name="email_subject", columnDefinition = "text", nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.8")
    @Getter
    @Setter
    private String subject;

    @Column(name="email_body", columnDefinition = "text", nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.9", multiLine = 10)
    @Getter
    @Setter
    private String body = "{{variables.payload}}";


    // *** IMPLEMENTATIONS ****

    private final static Comparator<EmailTarget> comparator = Comparator.comparing(EmailTarget::getName);

    @Override
    public int compareTo(final EmailTarget other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "organization", describedAs = "Update which Organization this belongs to")
    public EmailTarget updateOrganization(Organization organization) {
        setOrganization(organization);
        return this;
    }

    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        messageService.informUser(String.format("'%s' deleted", title));
        repositoryService.removeAndFlush(this);
    }
}
