package net.savantly.nexus.franchise.dom.location;


import static org.apache.causeway.applib.annotation.SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Navigable;
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
import net.savantly.nexus.franchise.FranchiseModule;


@Named(FranchiseModule.NAMESPACE + ".FranchiseLocationMember")
@javax.persistence.Entity
@javax.persistence.Table(
		schema=FranchiseModule.SCHEMA
	)
	@javax.persistence.EntityListeners(CausewayEntityListener.class)
	@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED)
	@DomainObjectLayout()
	@NoArgsConstructor(access = AccessLevel.PUBLIC)
	@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
	@ToString(onlyExplicitlyIncluded = true)
public class FranchiseLocationMember {

    @Inject @Transient RepositoryService repositoryService;
    @Inject @Transient TitleService titleService;
    @Inject @Transient MessageService messageService;
    
    public static FranchiseLocationMember withRequiredFields(FranchiseLocation location, FranchiseLocationMemberRole memberRole, HasUsername username) {
        val entity = new FranchiseLocationMember();
        entity.setLocation(location);
        entity.setMemberRole(memberRole);
        entity.setUserName(username.getUsername());
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
    @Getter @Setter
    @PropertyLayout()
    private FranchiseLocationMemberRole memberRole;

    @Title(sequence = "0", append = " - ")
    @Getter @Setter
	private String userName;

    @Getter @Setter
    @PropertyLayout(navigable = Navigable.PARENT)
	private FranchiseLocation location;
    
    @Action(semantics = NON_IDEMPOTENT_ARE_YOU_SURE)
    @ActionLayout(
            position = ActionLayout.Position.PANEL,
            describedAs = "Deletes this object from the persistent datastore")
    public void delete() {
        final String title = titleService.titleOf(this);
        this.location.getMembers().remove(this);
        repositoryService.removeAndFlush(this);
        messageService.informUser(String.format("'%s' deleted", title));
    }
}
