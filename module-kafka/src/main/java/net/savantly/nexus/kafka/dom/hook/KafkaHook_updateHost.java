package net.savantly.nexus.kafka.dom.hook;

import java.util.Set;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.ParameterLayout;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.PromptStyle;
import org.apache.causeway.applib.annotation.Publishing;
import org.apache.causeway.applib.annotation.SemanticsOf;

import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.kafka.dom.host.KafkaHost;
import net.savantly.nexus.kafka.dom.host.KafkaHostRepository;
import net.savantly.nexus.kafka.dom.userActions.KafkaUserActionService;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "host", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor(onConstructor_ = { @Inject })
public class KafkaHook_updateHost {

    final KafkaHook object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<KafkaHook_updateHost> {
    }

    @Inject
    KafkaUserActionService userActionService;
    @Inject
    KafkaHostRepository repository;

    @MemberSupport
    public KafkaHook act(
            @ParameterLayout(named = "Host") final KafkaHost host) {

        log.debug("updating hook host: {}", host);
        object.setHost(host);
        return object;
    }

    @MemberSupport
    public Set<KafkaHost> choices0Act() {
        return repository.findByOrganizationId(object.getOrganization().getId());
    }

}
