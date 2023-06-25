package net.savantly.nexus.orgweb.dom.organizationWebSite;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationWebSiteRepository extends JpaRepository<OrganizationWebSite, Long> {
    
    Set<OrganizationWebSite> findByOrganizationId(final String organizationId);

    Set<OrganizationWebSite> findByOrganizationIsNull();
}
