package net.savantly.nexus.kafka.api;

import net.savantly.nexus.kafka.dom.hook.KafkaHook;

public interface KafkaHookExecutor {

    KafkaHookExecutionResponse execute(KafkaHook webhook, String payload);
}
