package net.savantly.nexus.organizations.dom.organizationSecret;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationSecretRepository extends JpaRepository<OrganizationSecret, String>{

    Set<OrganizationSecret> findAllByOrganizationId(String id);
    
}
