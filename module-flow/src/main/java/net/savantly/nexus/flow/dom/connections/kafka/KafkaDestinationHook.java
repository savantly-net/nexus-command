package net.savantly.nexus.flow.dom.connections.kafka;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.causeway.applib.services.repository.RepositoryService;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.destination.AbstractBaseDestinationHook;
import net.savantly.nexus.flow.dom.destination.Destination;
import net.savantly.nexus.flow.dom.destination.DestinationHookResponse;
import net.savantly.nexus.flow.dom.flowContext.FlowContextFactory;
import net.savantly.nexus.flow.dom.formMapping.Mapping;
import net.savantly.nexus.flow.executor.javascript.JavascriptExecutor;
import net.savantly.nexus.kafka.dom.connection.KafkaTemplateFactory;
import net.savantly.nexus.kafka.dom.host.KafkaHosts;

@Log4j2
public class KafkaDestinationHook extends AbstractBaseDestinationHook {

    private final KafkaTemplateFactory kafkaTemplateFactory;
    private final KafkaHosts kafkaHosts;
    private final ObjectMapper objectMapper;

    public KafkaDestinationHook(FlowContextFactory flowContextFactory, JavascriptExecutor javascriptExecutor,
            RepositoryService repositoryService, ObjectMapper objectMapper,
            KafkaTemplateFactory kafkaTemplateFactory, KafkaHosts kafkaHosts) {

        super(flowContextFactory, javascriptExecutor, repositoryService, objectMapper);
        this.kafkaTemplateFactory = kafkaTemplateFactory;
        this.kafkaHosts = kafkaHosts;
        this.objectMapper = objectMapper;
    }

    public void close() throws Exception {
    }

    @Override
    public DestinationHookResponse sendData(Destination destination, Map<String, Object> payload,
            Collection<? extends Mapping> formMappings) {

        log.info("Sending data to Kafka destination: " + destination.getDestinationId());
        var host = kafkaHosts.findById(destination.getDestinationId()).orElseThrow();

        var template = kafkaTemplateFactory.getTemplate(host);

        try {
            var payloadString = objectMapper.writeValueAsString(payload);
            var res = template.send(
                    destination.getCollectionName(),
                    payloadString);
            res.get(5, TimeUnit.SECONDS);
            return new DestinationHookResponse().setSuccess(true).setMessage("Data sent to Kafka");
        } catch (Exception e) {
            log.error("Failed to send data to Kafka", e);
            return new DestinationHookResponse().setSuccess(false).setMessage(e.getMessage());
        }
    }

}
