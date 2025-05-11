package ru.job4j.kafka.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReqReply {

    private final long timeout;
    private String message = "";
    private boolean received = false;
    private Map<String,String> requestToCorrelationIdMap = new HashMap<>();

    public ReqReply(final long timeout) {
        this.timeout = timeout;
    }

    public String send(final String correlationId) {
        log.info("send C id "+correlationId);
        synchronized(correlationId) {
            if (!received) {
                try {
                    requestToCorrelationIdMap.put(correlationId,correlationId);
                    sendToKafka(correlationId);
                } catch (final InterruptedException ex) {
                    requestToCorrelationIdMap.remove(correlationId);
                    Thread.currentThread().interrupt();
                    message = "Interrupted while waiting";
                    return message;
                }
            }
            requestToCorrelationIdMap.remove(correlationId);
            return (received) ? message : "Happened timeout : "+timeout;
        }
    }

    public void receive(final String correlationId) {
        synchronized(requestToCorrelationIdMap.get(correlationId)) {
            received = true;
            message = correlationId;
            log.info("Notify  : "+correlationId);
            requestToCorrelationIdMap.get(correlationId).notifyAll();
        }
    }

    private void sendToKafka(final String correlationId) throws InterruptedException {
        correlationId.wait(timeout);
    }
}
