package net.savantly.nexus.projects.dom.projectPersona;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectPersonaRepository extends JpaRepository<ProjectPersona, String>{

    Set<ProjectPersona> findByProjectId(String id);
    
}
