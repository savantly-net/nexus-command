package net.savantly.nexus.gaform.dom.form;

import java.io.IOException;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.form.Form;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.IDEMPOTENT)
@ActionLayout(named = "Clear GA", promptStyle = PromptStyle.DIALOG, associateWith = "GoogleAnalytics", describedAs = "Clear the Google Analytics connection for this form")
@lombok.RequiredArgsConstructor
public class Form_clearGoogleAnalytics {

    final Form object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_clearGoogleAnalytics> {
    }

    @Inject
    MessageService messageService;
    @Inject
    FormGAConnectionRepository formConnectionRepository;

    @MemberSupport
    public Form act() throws IOException {
        formConnectionRepository.deleteByFormId(object.getId());
        return object;

    }
}
