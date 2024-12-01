package net.savantly.nexus.kafka.dom.listeners;

import java.util.Objects;

import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.applib.services.command.Command;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.publishing.spi.CommandSubscriber;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.common.api.AsJson;
import net.savantly.nexus.kafka.api.KafkaHookExecutor;
import net.savantly.nexus.kafka.dom.eventFilter.KafkaEventFilter;
import net.savantly.nexus.organizations.api.HasOrganization;

@Log4j2
@RequiredArgsConstructor
public class KafkaOnCommandListener implements CommandSubscriber {

    private final KafkaEventFilter eventFilter;
    private final KafkaHookExecutor kafkaHookExecutor;
    private final ObjectMapper objectMapper;
    private final InteractionService interactionService;

    @Inject
    private BookmarkService bookmarkService;

    private static final String SYSTEM_USER = "system";

    @Override
    public void onReady(Command command) {
    }

    @Override
    public void onStarted(Command command) {
    }

    @Override
    public void onCompleted(Command command) {
        log.debug("Command completed: {}", command);

        var commandResult = command.getResult();

        if (Objects.isNull(commandResult)) {
            return;
        }

        var featureId = command.getLogicalMemberIdentifier();

        interactionService.callAnonymous(() -> {
            var optObject = bookmarkService.lookup(commandResult);
            if (optObject.isEmpty()) {
                log.debug("Object not found: {}", commandResult);
                return null;
            }

            var object = optObject.get();

            if (!HasOrganization.class.isAssignableFrom(object.getClass())) {
                log.trace("Not associated with an organization: {}", commandResult);
                return null;
            }

            var orgObject = (HasOrganization) object;
            var organization = orgObject.getOrganization();
            var hooks = eventFilter.findKafkaHooksByFeatureIdAndWebhookOrganization(featureId, organization);

            for (var hook : hooks) {
                log.debug("Triggering hook: {}", hook);

                try {
                    String payload = "";
                    if (object instanceof String) {
                        payload = (String) object;
                    } else if (AsJson.class.isAssignableFrom(object.getClass())) {
                        payload = ((AsJson) object).asJson();
                    } else {
                        // TODO: how to handle the bookmarkService being null when the serializer is
                        // called?
                        payload = objectMapper.writeValueAsString(object);
                    }
                    kafkaHookExecutor.execute(hook, payload);
                } catch (Exception e) {
                    log.error("Error triggering hook", e);
                }
            }
            return null;
        });

    }

}
