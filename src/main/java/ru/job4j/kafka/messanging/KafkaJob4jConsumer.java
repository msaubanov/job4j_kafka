package ru.job4j.kafka.messanging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import ru.job4j.kafka.config.KafkaConstants;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Slf4j
public class KafkaJob4jConsumer {

    private final KafkaConsumer<String, String> consumer;

    public KafkaJob4jConsumer() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", KafkaConstants.SERVER);
        properties.put("key.deserializer", KafkaConstants.KEY_DESERIALIZER);
        properties.put("value.deserializer", KafkaConstants.VALUE_DESERIALIZER);
        properties.put("group.id", "my-consumer-group");
        properties.put("auto.offset.reset", "earliest");
        consumer = new KafkaConsumer<>(properties);
    }

    public void initConsume(final String topic) {
        consumer.subscribe(List.of(topic));
        try(consumer) {
            while (true) {
                consumer.poll(Duration.ofMillis(1000)).forEach(rec -> log.info("Received message: key="+rec.key()+", value="+rec.value()+", partition="+rec.partition()+", offset="+rec.offset()));
            }
        }catch (final Exception ex) {
            log.error("Consumer error :{}",ex.getMessage());
        }
    }
}
