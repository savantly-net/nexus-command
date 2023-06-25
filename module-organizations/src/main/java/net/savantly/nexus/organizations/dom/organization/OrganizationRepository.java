package net.savantly.nexus.organizations.dom.organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, String>{

    List<Organization> findByNameContainingIgnoreCase(String search);

    Organization findByName(String name);

    Organization findByNameIgnoreCase(String name);
    
}
