package net.savantly.nexus.franchise.dom.location;

import jakarta.inject.Named;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.causeway.applib.annotation.Bounding;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.DomainObjectLayout;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.jaxb.PersistentEntityAdapter;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.val;
import net.savantly.nexus.franchise.FranchiseModule;

@Named(FranchiseModule.NAMESPACE + ".FranchiseLocationMemberRole")
@jakarta.persistence.Entity
@jakarta.persistence.Table(
		schema=FranchiseModule.SCHEMA, name="franchise_location_member_role"
	)
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class FranchiseLocationMemberRole {
	
	public static FranchiseLocationMemberRole withRequiredFields(String name) {
        val entity = new FranchiseLocationMemberRole();
        entity.setName(name);
        return entity;
    }

    // *** PROPERTIES ***
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @PropertyLayout(fieldSetId = "metadata")
    @Column(name = "id", nullable = false)
    private Long id;

    @jakarta.persistence.Version
    @jakarta.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
	@PropertyLayout(fieldSetId = "name", sequence = "1")
	@Getter @Setter
	private String name;

}
