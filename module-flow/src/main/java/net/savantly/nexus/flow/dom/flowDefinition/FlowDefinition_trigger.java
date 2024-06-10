package net.savantly.nexus.flow.dom.flowDefinition;

import java.util.Objects;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Parameter;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.flowExecution.FlowExecution;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(describedAs = "Trigger", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FlowDefinition_trigger {

    final FlowDefinition object;
    final ObjectMapper objectMapper = new ObjectMapper();

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FlowDefinition_trigger> {
    }

    @Inject
    RepositoryService repositoryService;
    @Inject
    MessageService messageService;
    @Inject
    FlowDefinitionExecutionProxy flowDefinitionExecutionProxy;

    @MemberSupport
    public FlowExecution act(
            @ParameterLayout(multiLine = 20) @Parameter(optionality = Optionality.OPTIONAL) final String json) {

        Object payload;

        try {
            if (Objects.isNull(json) || json.isBlank()) {
                payload = null;
            } else {
                payload = objectMapper.readValue(json, Object.class);
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to parse json", e);
            messageService.raiseError("Failed to parse json: " + e.getMessage());
            return null;
        }

        return flowDefinitionExecutionProxy.executeFlow(object, payload);
    }

}
