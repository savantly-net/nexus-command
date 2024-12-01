package net.savantly.nexus.kafka.dom.hook;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KafkaHookRepository extends JpaRepository<KafkaHook, String> {

    List<KafkaHook> findByOrganizationId(String id);

}
