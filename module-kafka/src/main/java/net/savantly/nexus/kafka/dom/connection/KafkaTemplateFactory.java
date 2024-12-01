package net.savantly.nexus.kafka.dom.connection;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import net.savantly.nexus.kafka.dom.host.KafkaHost;

public class KafkaTemplateFactory {

    private Map<KafkaHost, KafkaTemplate<String, String>> kafkaTemplateCache = new HashMap<>();

    public KafkaTemplate<String, String> getTemplate(KafkaHost host) {
        if (hostInCache(host)) {
            return getKafkaTemplateFromCache(host);
        } else {
            KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory(host));
            cacheKafkaTemplate(host, kafkaTemplate);
            return kafkaTemplate;
        }
    }

    private KafkaTemplate<String, String> getKafkaTemplateFromCache(KafkaHost host) {
        return kafkaTemplateCache.get(host);
    }

    private boolean hostInCache(KafkaHost host) {
        return kafkaTemplateCache.containsKey(host);
    }

    private void cacheKafkaTemplate(KafkaHost host, KafkaTemplate<String, String> kafkaTemplate) {
        kafkaTemplateCache.put(host, kafkaTemplate);
    }

    private ProducerFactory<String, String> producerFactory(KafkaHost host) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, host.getHost());
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }
}
