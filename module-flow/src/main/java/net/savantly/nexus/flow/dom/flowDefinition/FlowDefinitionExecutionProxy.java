package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.stream.Collectors;

import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowContext.FlowContext;
import net.savantly.nexus.flow.dom.flowExecution.FlowExecution;
import net.savantly.nexus.flow.dom.flowSecret.FlowSecret;
import net.savantly.nexus.flow.dom.flowSecret.FlowSecretRepository;
import net.savantly.nexus.flow.executor.FlowExecutor;
import net.savantly.nexus.flow.executor.FlowExecutorFactory;

@RequiredArgsConstructor
public class FlowDefinitionExecutionProxy {

    private final FlowExecutorFactory flowExecutorFactory;
    private final FlowDefinitionRepository flowDefinitionRepository;
    private final ObjectMapper objectMapper;
    private final RepositoryService repositoryService;
    private final FlowSecretRepository flowSecretRepository;

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

        var context = FlowContext.empty();
        context.setVariable("payload", payload);

        var secrets = flowSecretRepository.findAllByOrganizationId(flow.getOrganization().getId());
        context.setSecrets(secrets.stream().collect(Collectors.toMap(FlowSecret::getName, FlowSecret::getSecret)));
        
        executor.execute(context);

        var executionAudit = FlowExecution.withContext(flow, context.serialize(objectMapper));

        return repositoryService.persist(executionAudit);
    }

    public FlowExecution executeFlow(FlowDefinition flow, Object payload) {

        FlowExecutor executor = flowExecutorFactory.createExecutor(flow.toDto());

        var context = FlowContext.empty();
        context.setVariable("payload", payload);

        var secrets = flowSecretRepository.findAllByOrganizationId(flow.getOrganization().getId());
        context.setSecrets(secrets.stream().collect(Collectors.toMap(FlowSecret::getName, FlowSecret::getSecret)));

        executor.execute(context);

        var executionAudit = FlowExecution.withContext(flow, context.serialize(objectMapper));

        return repositoryService.persist(executionAudit);
    }
}
