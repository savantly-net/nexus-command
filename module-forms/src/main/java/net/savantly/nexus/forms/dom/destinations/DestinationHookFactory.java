package net.savantly.nexus.forms.dom.destinations;

import org.springframework.boot.web.client.RestTemplateBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import net.savantly.nexus.forms.dom.connections.datasource.DatasourceFactory;
import net.savantly.nexus.forms.dom.connections.jdbcConnection.JdbcConnectionDestinationHook;
import net.savantly.nexus.forms.dom.connections.jdbcConnection.JdbcConnections;
import net.savantly.nexus.forms.dom.connections.webhook.WebhookDestinationHook;
import net.savantly.nexus.forms.dom.connections.webhook.Webhooks;

@RequiredArgsConstructor
public class DestinationHookFactory {

    final ObjectMapper objectMapper;
    final DatasourceFactory datasourceFactory;
    final JdbcConnections jdbcConnections;
    final Webhooks webhooks;
    final RestTemplateBuilder restTemplateBuilder;

    public DestinationHook createHook(Destination destination) {
        switch (destination.getDestinationType()) {
            case JDBC:
                return getJdbcHook(destination);

            case WEBHOOK:
                return getWebhookHook(destination);

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
}
