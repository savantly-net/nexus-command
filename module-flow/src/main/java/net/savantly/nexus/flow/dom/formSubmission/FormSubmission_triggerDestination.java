package net.savantly.nexus.flow.dom.formSubmission;

import java.util.List;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destination.Destination;
import net.savantly.nexus.flow.dom.destination.DestinationHookFactory;
import net.savantly.nexus.flow.dom.form.FormSubmissionProxy;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(promptStyle = PromptStyle.DIALOG, associateWith = "payload")
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FormSubmission_triggerDestination {

    final FormSubmission object;

    public static class ActionEvent
            extends
            org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<FormSubmission_triggerDestination> {
    }

    @Inject
    MessageService messageService;
    @Inject
    DestinationHookFactory destinationHookFactory;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    FormSubmissionProxy formSubmissionProxy;

    @MemberSupport
    public FormSubmission act(
            final Destination destination) {

        try {
            formSubmissionProxy.triggerDestinationForSubmission(object, destination.getId());
            messageService.informUser("Hook executed successfully: " + destination.getName());

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

    @MemberSupport
    public List<Destination> choices0Act() {
        return object.getForm().getDestinations().stream().toList();
    }

}
