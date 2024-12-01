package net.savantly.nexus.kafka.dom.hookTrigger;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KafkaHookTriggerRespository extends JpaRepository<KafkaHookTrigger, String> {

    List<KafkaHookTrigger> findByFeatureId(String featureId);

    @Query("select t from KafkaHookTrigger t where t.featureId = :featureId and t.hook.organization.id = :id")
    List<KafkaHookTrigger> findByFeatureIdAndHookOrganizationId(String featureId, String id);

}
