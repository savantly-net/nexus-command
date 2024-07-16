package net.savantly.nexus.flow.dom.emailTarget;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTargetRepository extends JpaRepository<EmailTarget, String>{

    Set<EmailTarget> findByOrganizationId(String id);
    
}
