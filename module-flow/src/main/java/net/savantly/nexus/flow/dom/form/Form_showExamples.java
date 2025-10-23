package net.savantly.nexus.flow.dom.form;

import java.io.IOException;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.value.Markup;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.SAFE_AND_REQUEST_CACHEABLE, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(named = "API Example", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class Form_showExamples {

    final Form object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_showExamples> {
    }

    @Inject
    MessageService messageService;

    @MemberSupport
    public Markup act() {

        // get the example from a file in the classpath
        var exampleFile = this.getClass().getResourceAsStream("example.html");

        // get the text from the file
        String exampleText;
        try {
            exampleText = new String(exampleFile.readAllBytes());
        } catch (IOException e) {
            messageService.raiseError("Failed to read example file");
            return null;
        }

        // replace the {{ENDPOINT}} with the actual endpoint
        var endpoint = String.format("https://[nexus-host]/api/public/flow/submit/%s", object.getId());

        var markup = Markup.valueOf(exampleText.replace("{{ENDPOINT}}", endpoint));
        return markup;
        
    }

}
