package net.savantly.nexus.flow.dom.flowDefinition;

import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.dom.flowExecution.FlowExecution;
import net.savantly.nexus.flow.executor.FlowExecutor;
import net.savantly.nexus.flow.executor.FlowExecutorFactory;

@RequiredArgsConstructor
public class FlowDefinitionExecutionProxy {

    private final FlowExecutorFactory flowExecutorFactory;
    private final FlowDefinitionRepository flowDefinitionRepository;
    private final ObjectMapper objectMapper;
    private final RepositoryService repositoryService;
    private final FlowContextFactory flowContextFactory;

    public FlowExecution executeFlow(String flowId, Object payload, String apiKey) {

        var flow = flowDefinitionRepository.findById(flowId)
                .orElseThrow(() -> new IllegalArgumentException("Flow not found"));
        if (!flow.isPublicAccess()) {
            if (apiKey == null) {
                throw new IllegalArgumentException("api-key required");
            }
            if (!flow.getApiKey().equals(apiKey)) {
                throw new IllegalArgumentException("api-key does not match");
            }
        }

        FlowExecutor executor = flowExecutorFactory.createExecutor(flow.toDto());

        var context = flowContextFactory.create(flow.getOrganization().getId());
        context.setVariable("payload", payload);
        
        executor.execute(context);

        var executionAudit = FlowExecution.withContext(flow, context.serialize(objectMapper));

        return repositoryService.persist(executionAudit);
    }

    public FlowExecution executeFlow(FlowDefinition flow, Object payload) {

        FlowExecutor executor = flowExecutorFactory.createExecutor(flow.toDto());

        var context = flowContextFactory.create(flow.getOrganization().getId());
        context.setVariable("payload", payload);

        executor.execute(context);

        var executionAudit = FlowExecution.withContext(flow, context.serialize(objectMapper));

        return repositoryService.persist(executionAudit);
    }
}
