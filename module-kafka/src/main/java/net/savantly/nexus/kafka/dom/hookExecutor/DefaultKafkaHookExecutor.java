package net.savantly.nexus.kafka.dom.hookExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.kafka.api.KafkaHookExecutionResponse;
import net.savantly.nexus.kafka.api.KafkaHookExecutor;
import net.savantly.nexus.kafka.dom.connection.KafkaTemplateFactory;
import net.savantly.nexus.kafka.dom.hook.KafkaHook;

@Log4j2
@RequiredArgsConstructor
public class DefaultKafkaHookExecutor implements KafkaHookExecutor {

    final KafkaTemplateFactory kafkaTemplateFactory;

    @Override
    public KafkaHookExecutionResponse execute(KafkaHook hook, String payload) {

        log.debug("Sending hook request: {}", payload);

        var template = kafkaTemplateFactory.getTemplate(hook.getHost());
        var result = template.send(hook.getTopic(), payload);

        try {
            var kResult = result.get(5, TimeUnit.SECONDS);
            log.debug("Sent to topic: {}", kResult.getRecordMetadata().topic());
            return new KafkaHookExecutionResponse().setSuccess(true)
                    .setMessage("sent to topic: " + kResult.getRecordMetadata().topic());
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error sending hook request", e);
            return new KafkaHookExecutionResponse().setSuccess(false)
                    .setMessage(e.getMessage());
        }

    }

}
