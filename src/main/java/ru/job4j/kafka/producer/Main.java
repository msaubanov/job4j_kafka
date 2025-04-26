package ru.job4j.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import ru.job4j.kafka.config.KafkaConstants;
import ru.job4j.kafka.messanging.KafkaJob4JProducerSetUp;

@Slf4j
public class Main {
    public static void main(final String[] args) {
        final KafkaJob4JProducerSetUp setUp = new KafkaJob4JProducerSetUp();
        final KafkaProducer<String, String> producer =  setUp.getProducer();
        try(producer) {
            for (int i = Integer.MAX_VALUE; i > 0; i--) {
                final String msg = "Task " + i;
                producer.send(new ProducerRecord<>(KafkaConstants.TOPIC, String.valueOf(i),msg));
                log.info("Send " + msg);
                Thread.sleep(1000);
            }
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
}
