package net.savantly.nexus.flow.dom.flowSecret;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowSecretRepository extends JpaRepository<FlowSecret, String>{

    Set<FlowSecret> findAllByOrganizationId(String id);
    
}
