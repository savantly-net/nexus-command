package net.savantly.nexus.flow.dom.connections.kafka;

import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.kafka.dom.connection.KafkaTemplateFactory;
import net.savantly.nexus.kafka.dom.host.KafkaHosts;

@RequiredArgsConstructor
public class KafkaDestinationHookFactory {

    private final FlowContextFactory flowContextFactory;
    private final JavascriptExecutor javascriptExecutor;
    private final RepositoryService repositoryService;
    private final ObjectMapper objectMapper;
    private final KafkaTemplateFactory kafkaTemplateFactory;
    private final KafkaHosts kafkaHosts;

    public KafkaDestinationHook createHook() {
        return new KafkaDestinationHook(flowContextFactory, javascriptExecutor, repositoryService, objectMapper,
                kafkaTemplateFactory, kafkaHosts);
    }
}
