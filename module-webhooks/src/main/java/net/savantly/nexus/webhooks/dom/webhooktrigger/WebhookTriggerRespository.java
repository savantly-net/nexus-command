package net.savantly.nexus.webhooks.dom.webhooktrigger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WebhookTriggerRespository extends JpaRepository<WebhookTrigger, String> {

    List<WebhookTrigger> findByFeatureId(String featureId);

    @Query("select t from WebhookTrigger t where t.featureId = :featureId and t.webhook.organization.id = :id")
    List<WebhookTrigger> findByFeatureIdAndWebhookOrganizationId(String featureId, String id);

}
