package net.savantly.nexus.flow.dom.destination;

import java.util.Collection;
import java.util.Map;

import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destinationExecution.DestinationExecution;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.dom.formMapping.Mapping;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractBaseDestinationHook implements DestinationHook {

    private final FlowContextFactory flowContextFactory;
    private final JavascriptExecutor javascriptExecutor;
    private final RepositoryService repositoryService;

    public abstract DestinationHookResponse sendData(Destination destination, Map<String, Object> payload,
            Collection<? extends Mapping> formMappings);

    protected Map<String, Object> transformPayload(Destination destination, Map<String, Object> payload) {
        log.info("Transforming payload for destination {}", destination.getName());
        var context = flowContextFactory.create(destination.getOrganization().getId());
        context.setVariable("payload", payload);

        if (destination.getTransformScript() != null) {
            try {
                var result = javascriptExecutor.execute(destination.getTransformScript(), context);
                log.info("Transform script result type: {}", result.getClass());
            } catch (Exception e) {
                log.error("Failed to execute transform script", e);
                throw new IllegalArgumentException("Failed to execute transform script. " + e.getMessage());
            }
        }
        return payload;
    }

    @Override
    public DestinationHookResponse execute(Destination destination, Map<String, Object> payload,
            Collection<? extends Mapping> formMappings) {

        var transformedPayload = transformPayload(destination, payload);

        var result = sendData(destination, transformedPayload, formMappings);
        var executionAudit = DestinationExecution.withResult(destination, result.isSuccess(), result.getMessage());

        repositoryService.persist(executionAudit);

        return result;
    }

}
