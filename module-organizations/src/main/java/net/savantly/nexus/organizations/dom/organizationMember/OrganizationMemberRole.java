package net.savantly.nexus.organizations.dom.organizationMember;

import java.util.UUID;

import jakarta.inject.Named;
import jakarta.persistence.Column;
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
import net.savantly.nexus.organizations.OrganizationsModule;

@Named(OrganizationsModule.NAMESPACE + ".OrganizationMemberRole")
@jakarta.persistence.Entity
@jakarta.persistence.Table(schema = OrganizationsModule.SCHEMA, name = "organization_member_role")
@jakarta.persistence.EntityListeners(CausewayEntityListener.class)
@DomainObject(entityChangePublishing = Publishing.ENABLED, editing = Editing.ENABLED, bounding = Bounding.BOUNDED)
@DomainObjectLayout()
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@XmlJavaTypeAdapter(PersistentEntityAdapter.class)
@ToString(onlyExplicitlyIncluded = true)
public class OrganizationMemberRole {

    public static OrganizationMemberRole withRequiredFields(String id, String name) {
        val entity = new OrganizationMemberRole();
        entity.id = id;
        entity.setName(name);
        return entity;
    }

    public static OrganizationMemberRole withName(String name) {
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
    @Getter
    @Setter
    private long version;

    @Title
    @PropertyLayout(fieldSetId = "name", sequence = "1")
    @Getter
    @Setter
    private String name;

}
