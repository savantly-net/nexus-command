package net.savantly.nexus.flow.dom.form;

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
import net.savantly.nexus.flow.dom.destinations.Destination;
import net.savantly.nexus.flow.dom.destinations.DestinationHookFactory;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "sampleData", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Form_testDestination {

    final Form object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_testDestination> {
    }

    @Inject
    MessageService messageService;
    @Inject
    DestinationHookFactory destinationHookFactory;
    @Inject
    ObjectMapper objectMapper;

    @MemberSupport
    public Form act(
            final Destination destination,
            @ParameterLayout(multiLine = 10) final String payload) {

        try {
            var payloadMap = objectMapper.readValue(payload, Map.class);
            var destinationHook = destinationHookFactory.createHook(destination);
            var result = destinationHook.execute(destination, payloadMap, object.getMappings());
            messageService.informUser("Hook executed successfully: " + result);

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

    @MemberSupport
    public List<Destination> choices0Act() {
        return object.getDestinations().stream().toList();
    }

    @MemberSupport
    public String default1Act() {

        if (object.getSampleData() != null) {
            return object.getSampleData();
        }

        return "{\n" +
                "  \"key\": \"value\"\n" +
                "}";
    }

}
