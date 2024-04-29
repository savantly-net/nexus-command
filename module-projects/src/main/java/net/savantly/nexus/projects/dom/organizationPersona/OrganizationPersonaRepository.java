package net.savantly.nexus.projects.dom.organizationPersona;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationPersonaRepository extends JpaRepository<OrganizationPersona, String>{

    Set<OrganizationPersona> findByOrganizationId(String id);
    
}
