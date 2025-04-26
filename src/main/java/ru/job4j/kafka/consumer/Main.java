package ru.job4j.kafka.consumer;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import java.time.Duration;
import java.util.List;

import ru.job4j.kafka.config.KafkaConstants;
import ru.job4j.kafka.messanging.KafkaJob4jConsumerSetUp;

@Slf4j
public class Main {
    public static void main(final String[] args) {
        final KafkaJob4jConsumerSetUp consumerSetUp = new KafkaJob4jConsumerSetUp();

        final KafkaConsumer<String, String> consumer = consumerSetUp.getConsumer();
        consumer.subscribe(List.of(KafkaConstants.TOPIC));
        try(consumer) {
            while (true) {
                consumer.poll(Duration.ofMillis(1000)).forEach(rec -> log.info("Received message: key="+rec.key()+", value="+rec.value()+", partition="+rec.partition()+", offset="+rec.offset()));
            }
        }catch (final Exception ex) {
            log.info("Consumer error :{}",ex.getMessage());
        }
    }
}
