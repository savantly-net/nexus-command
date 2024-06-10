package net.savantly.nexus.flow.dom.connections.webhook;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookRepository extends JpaRepository<Webhook, String> {

    Collection<Webhook> findByOrganizationId(String id);

}