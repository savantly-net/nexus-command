package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowDefinitionRepository extends JpaRepository<FlowDefinition, String>{

    Set<FlowDefinition> findByOrganizationId(String organizationId);
    
}
