package net.savantly.nexus.projects.dom.persona;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.title.TitleService;

@javax.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(describedAs = "Deletes the persona permanently")
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class Persona_delete {

    final Persona persona;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Persona_delete> {
    }

    @Inject
    PersonaRepository userRepository;
    @Inject
    TitleService titleService;
    @Inject
    MessageService messageService;

    @MemberSupport
    public void act() {

        final String title = titleService.titleOf(persona);
        messageService.informUser(String.format("'%s' deleted", title));
        userRepository.delete(persona);
    }

}
