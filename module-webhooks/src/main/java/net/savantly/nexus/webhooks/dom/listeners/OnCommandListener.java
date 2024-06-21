package net.savantly.nexus.webhooks.dom.listeners;

import java.util.Objects;

import org.apache.causeway.applib.services.bookmark.BookmarkService;
import org.apache.causeway.applib.services.command.Command;
import org.apache.causeway.applib.services.publishing.spi.CommandSubscriber;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.organizations.api.HasOrganization;
import net.savantly.nexus.webhooks.api.WebhookExecutor;
import net.savantly.nexus.webhooks.dom.eventFilter.EventFilter;

@Log4j2
@RequiredArgsConstructor
public class OnCommandListener implements CommandSubscriber {

    private final EventFilter eventFilter;
    private final WebhookExecutor webhookExecutor;
    private final BookmarkService bookmarkService;
    private final ObjectMapper objectMapper;

    @Override
    public void onReady(Command command) {
    }

    @Override
    public void onStarted(Command command) {
    }

    @Override
    public void onCompleted(Command command) {
        log.info("Command completed: {}", command);

        var featureId = command.getLogicalMemberIdentifier();

        if (Objects.isNull(command.getTarget())) {
            return;
        }

        var optObject = bookmarkService.lookup(command.getTarget());
        if (optObject.isEmpty()) {
            log.debug("Object not found: {}", command.getTarget());
            return;
        }

        var object = optObject.get();

        if (!HasOrganization.class.isAssignableFrom(object.getClass())) {
            log.debug("Not associated with an organization: {}", command.getTarget());
            return;
        }

        var orgObject = (HasOrganization) object;
        var organization = orgObject.getOrganization();
        var webhooks = eventFilter.findWebhooksByFeatureIdAndWebhookOrganization(featureId, organization);

        for (var webhook : webhooks) {
            log.debug("Triggering Webhook: {}", webhook);

            try {
                var payload = objectMapper.writeValueAsString(object);
                webhookExecutor.execute(webhook, payload);
            } catch (Exception e) {
                log.error("Error triggering webhook", e);
            }
        }

    }

}
