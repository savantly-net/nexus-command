package net.savantly.nexus.flow.dom.destinationExecution;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DestinationExecutionRepository extends JpaRepository<DestinationExecution, String> {

    Set<DestinationExecution> findByDestinationId(String id);

    void deleteAllByDestinationId(String id);

}
