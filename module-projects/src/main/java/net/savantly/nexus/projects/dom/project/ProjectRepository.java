package net.savantly.nexus.projects.dom.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String>{

    List<Project> findByNameContainingIgnoreCase(String search);

    Project findByName(String name);

    Project findByNameIgnoreCase(String name);
    
}
