package net.savantly.nexus.organizations.dom.organizationMember;

import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.applib.mixins.security.HasUsername;
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
import net.savantly.nexus.organizations.OrganizationsModule;
import net.savantly.nexus.organizations.dom.organization.Organization;

@Named(OrganizationsModule.NAMESPACE + ".OrganizationMember")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
		schema=OrganizationsModule.SCHEMA
	)
	@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
	@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
	@DomainObjectLayout(describedAs = "A member of an organization")
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
	@ToString(onlyExplicitlyIncluded = true)
public class OrganizationMember {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static OrganizationMember withRequiredFields(Organization organization, OrganizationMemberRole memberRole, HasUsername username) {
        val entity = new OrganizationMember();
        entity.setOrganization(organization);
        entity.setMemberRole(memberRole);
        entity.setUserName(username.getUsername());
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title(sequence = "1")
    @Getter @Setter
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    @jakarta.persistence.Column(name = "username", nullable = false)
	private String userName;

    @Title(sequence = "2", prepend = " as ")
    @Getter @Setter
    @PropertyLayout(fieldSetId = "name", sequence = "2")
    private OrganizationMemberRole memberRole;


    @Getter @Setter
	private Organization organization;
    
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        this.organization.getMembers().remove(this);
        repositoryService.removeAndFlush(this);
        messageService.informUser(String.format("'%s' deleted", title));
    }
}
