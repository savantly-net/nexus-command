package net.savantly.nexus.webhooks.dom.webhook;


import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Collection;
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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.api.OrganizationEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.webhooks.WebhooksModule;
import net.savantly.nexus.webhooks.dom.webhooktrigger.WebhookTrigger;

@Named(WebhooksModule.NAMESPACE + ".Webhook")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = WebhooksModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
@DomainObjectLayout(cssClassFa = "hook")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class Webhook extends OrganizationEntity implements Comparable<Webhook> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static Webhook withName(Organization organization, String name) {
        val entity = new Webhook();
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

    @Column(length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5")
    @Getter
    @Setter
    private String url = "https://savantly/api/webhook";

    @Column(name = "method", length = 255, nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.6")
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private WebhookMethodType method = WebhookMethodType.POST;

    @Column(columnDefinition="text", nullable = true)
    @PropertyLayout(fieldSetId = "identity", sequence = "1.7", multiLine = 10)
    @Getter
    @Setter
    private String headers = "Content-type: application/json";

    @JoinColumn(name = "trigger_id", nullable = false)
    @Collection
    @Getter
    @Setter
    @OneToMany(mappedBy = "webhook", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<WebhookTrigger> triggers = new HashSet<>();

    // *** IMPLEMENTATIONS ****

    private final static Comparator<Webhook> comparator = Comparator.comparing(Webhook::getName);

    @Override
    public int compareTo(final Webhook other) {
        return comparator.compare(this, other);
    }

    // *** ACTIONS ***

    @Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
    @ActionLayout(associateWith = "organization", describedAs = "Update which Organization this belongs to")
    public Webhook updateOrganization(Organization organization) {
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
