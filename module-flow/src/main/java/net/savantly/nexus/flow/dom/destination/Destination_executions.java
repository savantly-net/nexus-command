package net.savantly.nexus.flow.dom.destination;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Transient;
import net.savantly.nexus.audited.api.AuditedEntitySortedByDateDesc;
import net.savantly.nexus.flow.dom.destinationExecution.DestinationExecution;
import net.savantly.nexus.flow.dom.destinationExecution.DestinationExecutionRepository;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
@Named("Destination_executions")
public class Destination_executions {

    final Destination object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Destination_executions> {
    }

    @Inject
    @Transient
    DestinationExecutionRepository repository;

    @CollectionLayout(named = "Logs", describedAs = "Executions of this flow definition", sequence = "99", sortedBy = AuditedEntitySortedByDateDesc.class)
    public Set<DestinationExecution> coll() {
        return repository.findByDestinationId(object.getId());
    }
}
