package ru.job4j.kafka.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReqReply {

    private final long timeout;
    private String message = "";
    private boolean received = false;

    public ReqReply(final long timeout) {
        this.timeout = timeout;
    }

    public synchronized String send(final String correlationId) {
        if (!received) {
            try {
                sendToKafka(correlationId);
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
                message = "Interrupted while waiting";
                return message;
            }
        }
        return (received) ? message : "Happened timeout : "+timeout;
    }

    public synchronized void receive(final String correlationId) {
        received = true;
        message = correlationId;
        notifyAll();
    }

    private void sendToKafka(final String correlationId) throws InterruptedException {
        wait(timeout);
    }
}
