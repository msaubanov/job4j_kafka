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

    public String send(final String correlationId) {
        log.info("send C id "+correlationId);
        synchronized(correlationId) {
            if (!received) {
                try {
                    correlationId.wait(timeout);
                } catch (final InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    message = "Interrupted while waiting";
                    return message;
                }
            }
            return (received) ? message : "Happened timeout : "+timeout;
        }
    }

    public void receive(final String correlationId) {
        log.info("Receive : {}",correlationId);
        received = true;
        message = correlationId;
        log.info("Notify  : "+correlationId);
        correlationId.notifyAll();
    }
}
