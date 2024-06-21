package net.savantly.nexus.webhooks.dom.eventFilter;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.webhooks.dom.webhook.Webhook;
import net.savantly.nexus.webhooks.dom.webhooktrigger.WebhookTriggerRespository;

@Log4j2
@RequiredArgsConstructor
public class EventFilter {

    private final WebhookTriggerRespository webhookTriggerRespository;

    public List<Webhook> findWebhooksByFeatureId(String featureId) {

        var triggers = webhookTriggerRespository.findByFeatureId(featureId);
        return triggers.stream().map(trigger -> trigger.getWebhook()).toList();

    }

    public List<Webhook> findWebhooksByFeatureIdAndWebhookOrganization(String featureId, Organization organization) {
        var triggers = webhookTriggerRespository.findByFeatureIdAndWebhookOrganizationId(featureId,
                organization.getId());
        return triggers.stream().map(trigger -> trigger.getWebhook()).toList();
    }

}
