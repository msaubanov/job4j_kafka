package ru.job4j.kafka.service;


import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class ReqReplyService {

    private final Map<String, Pair<String,ReqReply> > callbackStorage;
    private final long timeout = 2000;
    public ReqReplyService(final Map<String, Pair<String,ReqReply>> callbackStorage) {
        this.callbackStorage = callbackStorage;
    }

    public String send(final String correlationId) {
        final String response;
        ReqReply reqReply = new ReqReply(timeout);
        callbackStorage.put(correlationId, new Pair<>(correlationId,reqReply));
        response = reqReply.send(correlationId);
        return response;
    }

    public void receive (final String correlationId) {
        log.info("RECEIVE IN SERVICE : {}", correlationId);
        final Pair<String,ReqReply> pair = callbackStorage.get(correlationId);
        final ReqReply protoCommand = pair.getSecond();
        protoCommand.receive(pair.getFirst());
        callbackStorage.remove(correlationId);
    }

    public static class ReqReply {

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
}
