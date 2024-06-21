package net.savantly.nexus.webhooks.dom.webhook;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookRepository extends JpaRepository<Webhook, String> {

    List<Webhook> findByOrganizationId(String id);

}
