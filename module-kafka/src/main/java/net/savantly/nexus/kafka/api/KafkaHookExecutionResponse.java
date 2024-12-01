package net.savantly.nexus.kafka.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class KafkaHookExecutionResponse {

    private boolean success;
    private String message;
}
