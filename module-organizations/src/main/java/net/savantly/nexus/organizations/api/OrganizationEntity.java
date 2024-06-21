package net.savantly.nexus.organizations.api;

import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.Navigable;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Where;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import net.savantly.nexus.audited.api.AuditedEntity;
import net.savantly.nexus.organizations.dom.organization.Organization;

@MappedSuperclass
public abstract class OrganizationEntity extends AuditedEntity implements HasOrganization {

    @JoinColumn(name = "org_id", nullable = false)
    @Property(editing = Editing.DISABLED)
    @PropertyLayout(fieldSetId = "identity", sequence = "2", navigable = Navigable.PARENT, hidden = Where.ALL_TABLES)
    @Getter
    @Setter
    private Organization organization;
}
