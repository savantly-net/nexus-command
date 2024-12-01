package net.savantly.nexus.flow.dom.destination;

import org.springframework.boot.web.client.RestTemplateBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.connections.datasource.DatasourceFactory;
import net.savantly.nexus.flow.dom.connections.flowHook.FlowDestinationHookFactory;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnectionDestinationHook;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnections;
import net.savantly.nexus.flow.dom.connections.kafka.KafkaDestinationHookFactory;
import net.savantly.nexus.flow.dom.connections.webhook.WebhookDestinationHookFactory;
import net.savantly.nexus.flow.dom.emailTarget.EmailDestinationHookFactory;

@RequiredArgsConstructor
@Log4j2
public class DestinationHookFactory {

    final ObjectMapper objectMapper;
    final DatasourceFactory datasourceFactory;
    final JdbcConnections jdbcConnections;
    final RestTemplateBuilder restTemplateBuilder;
    final FlowDestinationHookFactory flowDestinationHookFactory;
    final EmailDestinationHookFactory emailDestinationHookFactory;
    final WebhookDestinationHookFactory webhookDestinationHookFactory;
    final KafkaDestinationHookFactory kafkaDestinationHookFactory;

    public DestinationHook createHook(Destination destination) {
        log.info("Creating destination hook for: " + destination.getDestinationType());
        switch (destination.getDestinationType()) {
            case JDBC:
                return getJdbcHook(destination);

            case WEBHOOK:
                return getWebhookHook(destination);
            case FLOW:
                return getFlowDestinationHook(destination);

            case EMAIL:
                return getEmailDestinationHook(destination);

            case KAFKA:
                return getKafkaDestinationHook(destination);

            default:
                throw new RuntimeException("Destination type not supported");
        }
    }

    private DestinationHook getJdbcHook(Destination destination) {
        var opt = jdbcConnections.findById(destination.getDestinationId());
        if (opt.isEmpty()) {
            throw new RuntimeException("JDBC Connection not found");
        }
        var ds = datasourceFactory.createFromJdbcConnection(opt.get());
        return new JdbcConnectionDestinationHook(ds, objectMapper);
    }

    private DestinationHook getWebhookHook(Destination destination) {
        return webhookDestinationHookFactory.createHook();
    }

    private DestinationHook getFlowDestinationHook(Destination destination) {
        return flowDestinationHookFactory.createHook();
    }

    private DestinationHook getEmailDestinationHook(Destination destination) {
        return emailDestinationHookFactory.createHook();
    }

    private DestinationHook getKafkaDestinationHook(Destination destination) {
        return kafkaDestinationHookFactory.createHook();
    }
}
