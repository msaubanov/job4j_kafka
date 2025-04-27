package ru.job4j.kafka.producer;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.kafka.messanging.KafkaJob4JProducer;

@Slf4j
public class Main {
    public static void main(final String[] args) {
        final KafkaJob4JProducer setUp = new KafkaJob4JProducer();
        setUp.startProducer();
    }
}
