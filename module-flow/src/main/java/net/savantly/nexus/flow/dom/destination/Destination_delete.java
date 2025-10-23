package net.savantly.nexus.flow.dom.destination;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.repository.RepositoryService;
import org.apache.causeway.applib.services.title.TitleService;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destinationExecution.DestinationExecutionRepository;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "destinations", describedAs = "Deletes this object from the persistent datastore", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class Destination_delete {

    final Destination object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Destination_delete> {
    }

    @Inject
    MessageService messageService;
    @Inject
    DestinationExecutionRepository destinationExecutionRepository;
    @Inject
    TitleService titleService;
    @Inject
    RepositoryService repositoryService;

    @MemberSupport
    public Destination act() {
        try {
            final String title = titleService.titleOf(object);
            destinationExecutionRepository.deleteAllByDestinationId(object.getId());
            repositoryService.removeAndFlush(object);

            messageService.informUser(String.format("'%s' deleted", title));

        } catch (Exception e) {
            messageService.raiseError("Failed to execute destination hook: " + e.getMessage());
        }

        return object;
    }

}
