package net.savantly.nexus.organizations.dom.organizationUser;

import java.util.Comparator;

import jakarta.inject.Named;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.Title;
import org.apache.causeway.applib.mixins.security.HasUsername;

import lombok.Getter;
import lombok.Setter;
import net.savantly.nexus.organizations.OrganizationsModule;

@Named(OrganizationsModule.NAMESPACE + ".OrganizationUser")
@DomainObject(nature = Nature.VIEW_MODEL)
@XmlRootElement(name = "OrganizationUser")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
public class OrganizationUser implements HasUsername, Comparable<OrganizationUser> {
    
    private String username;

    @Title
    private String displayName;

    // *** IMPLEMENTATIONS ****

    private final static Comparator<OrganizationUser> comparator =
            Comparator.comparing(OrganizationUser::getUsername);

    @Override
    public int compareTo(final OrganizationUser other) {
        return comparator.compare(this, other);
    }
}
