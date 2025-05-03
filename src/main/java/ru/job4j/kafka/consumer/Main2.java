package ru.job4j.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.kafka.config.KafkaConstants;
import ru.job4j.kafka.messanging.KafkaJob4jConsumer2;

@Slf4j
public class Main2 {
    public static void main(final String[] args) {
        final KafkaJob4jConsumer2 consumerSetUp = new KafkaJob4jConsumer2();
        consumerSetUp.initConsume(KafkaConstants.TOPIC);
    }
}
