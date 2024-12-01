package net.savantly.nexus.kafka.dom.host;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaHostRepository extends JpaRepository<KafkaHost, String> {

    Set<KafkaHost> findByOrganizationId(String organizationId);

}
