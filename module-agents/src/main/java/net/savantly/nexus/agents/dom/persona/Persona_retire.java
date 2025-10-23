package net.savantly.nexus.agents.dom.persona;

import jakarta.inject.Inject;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.message.MessageService;
import org.apache.causeway.applib.services.title.TitleService;

@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(describedAs = "Deletes the persona permanently")
@lombok.RequiredArgsConstructor
public class Persona_retire {

    final Persona persona;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<Persona_retire> {
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
