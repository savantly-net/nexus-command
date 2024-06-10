package net.savantly.nexus.flow.dom.flowExecution;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowExecutionRepository extends JpaRepository<FlowExecution, String>{

    Set<FlowExecution> findByFlowDefinitionId(String id);
    
}
