package net.savantly.nexus.organizations.dom.organizationSecret;


import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import java.util.Comparator;
import java.util.UUID;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.annotation.Where;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.common.types.Name;
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.api.OrganizationEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(OrganizationsModule.NAMESPACE + ".OrganizationSecret")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = OrganizationsModule.SCHEMA)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.DISABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout(cssClassFa = "user-secret")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class OrganizationSecret extends OrganizationEntity implements Comparable<OrganizationSecret> {

    @Inject
    @Transient
    RepositoryService repositoryService;
    @Inject
    @Transient
    TitleService titleService;
    @Inject
    @Transient
    MessageService messageService;

    public static OrganizationSecret withName(Organization organization, String name) {
        val entity = new OrganizationSecret();
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
    @Column(length = Name.MAX_LEN, nullable = false)
    @Property(regexPattern = "^[a-zA-Z0-9_]*$", regexPatternReplacement = "Name must be alphanumeric or underscore", maxLength = Name.MAX_LEN)
    @Getter
    @Setter
    @ToString.Include
    @PropertyLayout(fieldSetId = "identity", sequence = "1")
    private String name;


    @Column(columnDefinition = "text", nullable = true)
    @Property
    @PropertyLayout(fieldSetId = "identity", sequence = "1.5")
    private String secret;

    public void setEncryptedSecret(String secret) {
        this.secret = secret;
    }

    // just so we see it's set in the UI
    public String getSecret() {
        if (secret == null || secret.isEmpty()) {
            return "unset";
        }
        return "[encrypted]";
    }

    @Transient
    @PropertyLayout(hidden = Where.EVERYWHERE)
    public String getEncryptedSecret() {
        return secret;
    }


    // *** IMPLEMENTATIONS ****

    private final static Comparator<OrganizationSecret> comparator = Comparator.comparing(OrganizationSecret::getName);

    @Override
    public int compareTo(final OrganizationSecret other) {
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
    
}
