package net.savantly.nexus.gaform.dom.form;

import java.io.IOException;
import java.util.List;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.applib.services.message.MessageService;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.form.Form;
import net.savantly.nexus.ga.dom.gaConnection.GAConnection;
import net.savantly.nexus.ga.dom.gaConnection.GAConnectionRepository;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(named = "Update GA", promptStyle = PromptStyle.DIALOG, associateWith = "GoogleAnalytics", describedAs = "Update the Google Analytics connection for this form")
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Form_updateGoogleAnalytics {

    final Form object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Form_updateGoogleAnalytics> {
    }

    @Inject
    MessageService messageService;
    @Inject
    FactoryService factoryService;
    @Inject
    FormGAConnectionRepository formConnectionRepository;
    @Inject
    GAConnectionRepository gaConnectionRepository;

    @MemberSupport
    public Form act(final GAConnection gaConnection) throws IOException {
        formConnectionRepository.deleteByFormId(object.getId());
        FormGAConnection formGAConnection = FormGAConnection.withRequiredArgs(object, gaConnection);
        formConnectionRepository.save(formGAConnection);

        messageService.informUser("Google Analytics connection updated for form " + object.getName());
        return object;

    }

    @MemberSupport
    public List<GAConnection> choices0Act() {
        return gaConnectionRepository.findByOrganizationId(object.getOrganization().getId());
    }

}
