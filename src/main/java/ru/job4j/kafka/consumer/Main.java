package ru.job4j.kafka.consumer;


import lombok.extern.slf4j.Slf4j;
import ru.job4j.kafka.config.KafkaConstants;
import ru.job4j.kafka.messanging.KafkaJob4jConsumer;

@Slf4j
public class Main {

    public static void main(final String[] args) {
        final KafkaJob4jConsumer consumerSetUp = new KafkaJob4jConsumer();
        consumerSetUp.initConsume(KafkaConstants.TOPIC);
    }
}
