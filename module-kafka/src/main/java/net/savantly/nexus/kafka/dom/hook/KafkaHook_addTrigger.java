package net.savantly.nexus.kafka.dom.hook;

import java.util.List;

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
import net.savantly.nexus.kafka.dom.userActions.KafkaUserActionService;
import net.savantly.nexus.kafka.dom.hookTrigger.KafkaHookTrigger;

@Log4j2
@jakarta.annotation.Priority(PriorityPrecedence.EARLY)
@Action(semantics = SemanticsOf.NON_IDEMPOTENT, commandPublishing = Publishing.ENABLED, executionPublishing = Publishing.ENABLED)
@ActionLayout(associateWith = "members", promptStyle = PromptStyle.DIALOG)
@lombok.RequiredArgsConstructor
public class KafkaHook_addTrigger {

    final KafkaHook object;

    public static class ActionEvent
            extends org.apache.causeway.applib.CausewayModuleApplib.ActionDomainEvent<KafkaHook_addTrigger> {
    }

    @Inject
    KafkaUserActionService userActionService;

    @MemberSupport
    public KafkaHook act(
            @ParameterLayout(named = "Object") final String featureId) {

        log.debug("creating hook trigger: {}", featureId);
        var trigger = KafkaHookTrigger.withRequiredArgs(object, featureId);

        log.debug("adding trigger to hook: {}", trigger);
        this.object.getTriggers().add(trigger);
        return object;
    }

    @MemberSupport
    public List<String> choices0Act(String search) {
        return userActionService.visibleDomainMembersForUser();
    }

}
