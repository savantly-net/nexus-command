package net.savantly.franchise.dom.location;

import javax.inject.Named;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
import net.savantly.franchise.FranchiseModule;

@Named(FranchiseModule.NAMESPACE + ".FranchiseLocationMemberRole")
@javax.persistence.Entity
@javax.persistence.Table(
		schema=FranchiseModule.SCHEMA, name="franchise_location_member_role"
	)
@javax.persistence.EntityListeners(CausewayEntityListener.class)
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
    @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @PropertyLayout(fieldSetId = "metadata")
    @Column(name = "id", nullable = false)
    private Long id;

    @javax.persistence.Version
    @javax.persistence.Column(name = "version", nullable = false)
    @PropertyLayout(fieldSetId = "metadata", sequence = "999")
    @Getter @Setter
    private long version;

    @Title
	@PropertyLayout(fieldSetId = "name", sequence = "1")
	@Getter @Setter
	private String name;

}
