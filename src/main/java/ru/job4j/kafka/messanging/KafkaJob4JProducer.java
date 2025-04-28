package ru.job4j.kafka.messanging;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.job4j.kafka.config.KafkaConstants;

import java.util.Properties;

@Slf4j
public class KafkaJob4JProducer {

    private final KafkaProducer<String, String> producer;

    public KafkaJob4JProducer() {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", KafkaConstants.SERVER);
        properties.put("key.serializer", KafkaConstants.KEY_SERIALIZER);
        properties.put("value.serializer", KafkaConstants.VALUE_SERIALIZER);
        producer = new KafkaProducer<>(properties);
    }

    public void startProducer() {
        try(producer) {
            for (int i = Integer.MAX_VALUE; i > 0; i--) {
                final String msg = "Task " + i;
                producer.send(new ProducerRecord<>(KafkaConstants.TOPIC, String.valueOf(i),msg));
                log.info("Send " + msg);
                Thread.sleep(1000);
            }
        } catch (final InterruptedException ex) {
            log.error("Error in producer : {}", ex.getMessage());
        }
    }
}
