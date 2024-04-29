package net.savantly.nexus.projects.dom.persona;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, String>{

    Persona findByNameIgnoreCase(String name);

    Collection<Persona> findByNameContainingIgnoreCase(String search);
    
}
