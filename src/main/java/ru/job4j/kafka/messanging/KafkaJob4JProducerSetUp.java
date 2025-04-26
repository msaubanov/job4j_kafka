package ru.job4j.kafka.messanging;

import org.apache.kafka.clients.producer.KafkaProducer;
import ru.job4j.kafka.config.KafkaConstants;

import java.util.Properties;

public class KafkaJob4JProducerSetUp {

    private final KafkaProducer<String, String> producer;

    public KafkaJob4JProducerSetUp() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", KafkaConstants.SERVER);
        properties.put("key.serializer", KafkaConstants.KEY_SERIALIZER);
        properties.put("value.serializer", KafkaConstants.VALUE_SERIALIZER);
        producer = new KafkaProducer<>(properties);
    }

    public KafkaProducer<String, String> getProducer() {
        return producer;
    }
}
