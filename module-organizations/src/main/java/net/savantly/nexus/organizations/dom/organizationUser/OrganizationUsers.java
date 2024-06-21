package net.savantly.nexus.organizations.dom.organizationUser;

import java.util.List;

import net.savantly.nexus.organizations.dom.organization.Organization;

public interface OrganizationUsers {
    
    List<OrganizationUser> findAll();
    boolean isMemberOfOrganization(String username, Organization organization);
    List<Organization> findOrganizationsByUsername(String username);
}
