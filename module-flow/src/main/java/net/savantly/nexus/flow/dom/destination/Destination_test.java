package net.savantly.nexus.flow.dom.destination;

import java.util.List;
import java.util.Map;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "destinations", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Destination_test {

    final Destination object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Destination_test> {
    }

    @Inject
    MessageService messageService;
    @Inject
    DestinationHookFactory destinationHookFactory;
    @Inject
    ObjectMapper objectMapper;

    @MemberSupport
    public Destination act(@ParameterLayout(multiLine = 10) final String payload) {
        try {
            var payloadMap = objectMapper.readValue(payload, Map.class);
            var destinationHook = destinationHookFactory.createHook(object);
            var result = destinationHook.execute(object, payloadMap, List.of());
            messageService.informUser("Hook executed successfully: " + result);

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

    public String default0Act() {
        return "{\n" + "  \"key\": \"value\"\n" + "}";
    }

}
