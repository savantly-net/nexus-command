package net.savantly.nexus.kafka.dom.eventFilter;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.organizations.dom.organization.Organization;
import net.savantly.nexus.kafka.dom.hook.KafkaHook;
import net.savantly.nexus.kafka.dom.hookTrigger.KafkaHookTriggerRespository;

@Log4j2
@RequiredArgsConstructor
public class KafkaEventFilter {

    private final KafkaHookTriggerRespository kafkaHookTriggerRespository;

    public List<KafkaHook> findKafkaHooksByFeatureId(String featureId) {

        var triggers = kafkaHookTriggerRespository.findByFeatureId(featureId);
        return triggers.stream().map(trigger -> trigger.getHook()).toList();

    }

    public List<KafkaHook> findKafkaHooksByFeatureIdAndWebhookOrganization(String featureId,
            Organization organization) {
        var triggers = kafkaHookTriggerRespository.findByFeatureIdAndHookOrganizationId(featureId,
                organization.getId());
        return triggers.stream().map(trigger -> trigger.getHook()).toList();
    }

}
