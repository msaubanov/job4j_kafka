package ru.job4j.kafka.messanging;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.job4j.kafka.config.KafkaConstants;

import java.util.Properties;

public class KafkaJob4jConsumerSetUp {

    private final KafkaConsumer<String, String> consumer;

    public KafkaJob4jConsumerSetUp() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", KafkaConstants.SERVER);
        properties.put("key.deserializer", KafkaConstants.KEY_DESERIALIZER);
        properties.put("value.deserializer", KafkaConstants.VALUE_DESERIALIZER);
        properties.put("group.id", "my-consumer-group");
        properties.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(properties);
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }
}
