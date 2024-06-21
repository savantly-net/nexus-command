package net.savantly.nexus.flow.dom.form;

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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "submissions", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Form_addSubmission {

    final Form form;

    @Inject
    FormSubmissionProxy formSubmissionProxy;
    @Inject
    ObjectMapper objectMapper;
    @Inject
    MessageService messageService;

    private final TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {
    };

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_addSubmission> {
    }

    @MemberSupport
    public Form act(
            @ParameterLayout(multiLine = 10, describedAs = "The json payload") final String body) {

        try {
            var payload = objectMapper.readValue(body, mapType);
            formSubmissionProxy.submitForm(form, payload);
        } catch (Exception e) {
            messageService.raiseError("Failed to submit form: " + e.getMessage());
        }

        return form;
    }

    @MemberSupport
    public String validate0Act(String body) {
        try {
            objectMapper.readValue(body, mapType);
        } catch (Exception e) {
            return "Invalid JSON: " + e.getMessage();
        }
        return null;
    }

}
