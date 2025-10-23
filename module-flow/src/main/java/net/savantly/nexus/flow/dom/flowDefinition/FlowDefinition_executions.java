package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.Set;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.Transient;
import net.savantly.nexus.flow.dom.flowExecution.FlowExecution;
import net.savantly.nexus.flow.dom.flowExecution.FlowExecutionRepository;

@Collection
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@lombok.RequiredArgsConstructor
@Named("FlowDefinition_executions")
public class FlowDefinition_executions {

    final FlowDefinition object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FlowDefinition_executions> {
    }

    @Inject
    @Transient
    FlowExecutionRepository repository;

    @CollectionLayout(named = "Executions", describedAs = "Executions of this flow definition", sequence = "99")
    public Set<FlowExecution> coll() {
        return repository.findByFlowDefinitionId(object.getId());
    }
}
