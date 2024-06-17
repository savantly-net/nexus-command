package net.savantly.nexus.flow.dom.destinations;

import org.springframework.boot.web.client.RestTemplateBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.savantly.nexus.flow.dom.connections.datasource.DatasourceFactory;
import net.savantly.nexus.flow.dom.connections.flowHook.FlowDestinationHookFactory;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnectionDestinationHook;
import net.savantly.nexus.flow.dom.connections.jdbcConnection.JdbcConnections;
import net.savantly.nexus.flow.dom.connections.webhook.WebhookDestinationHook;
import net.savantly.nexus.flow.dom.connections.webhook.Webhooks;

@RequiredArgsConstructor
@Log4j2
public class DestinationHookFactory {

    final ObjectMapper objectMapper;
    final DatasourceFactory datasourceFactory;
    final JdbcConnections jdbcConnections;
    final Webhooks webhooks;
    final RestTemplateBuilder restTemplateBuilder;
    final FlowDestinationHookFactory flowDestinationHookFactory;

    public DestinationHook createHook(Destination destination) {
        log.info("Creating destination hook for: " + destination.getDestinationType());
        switch (destination.getDestinationType()) {
            case JDBC:
                return getJdbcHook(destination);

            case WEBHOOK:
                return getWebhookHook(destination);
            case FLOW:
                return getFlowDestinationHook(destination);

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
        var opt = webhooks.findById(destination.getDestinationId());
        if (opt.isEmpty()) {
            throw new RuntimeException("Webhook not found");
        }
        return new WebhookDestinationHook(webhooks, restTemplateBuilder);
    }

    private DestinationHook getFlowDestinationHook(Destination destination) {
        return flowDestinationHookFactory.createHook();
    }
}
