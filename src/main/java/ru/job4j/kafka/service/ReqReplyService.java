package ru.job4j.kafka.service;


import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ReqReplyService {

    private final Map<String, Pair<String,ReqReply> > callbackStorage = new ConcurrentHashMap<>();
    private final long timeout;
    public ReqReplyService(long timeout) {
        this.timeout = timeout;
    }

    public String send(final String correlationId, String payload) {
        final String response;
        ReqReply reqReply = new ReqReply();
        reqReply.setTimeout(this.timeout);
        callbackStorage.put(correlationId, new Pair<>(payload,reqReply));
        response = reqReply.send(payload);
        return response;
    }

    public void receive (final String correlationId) {
        log.info("RECEIVE IN SERVICE : {}", correlationId);
        final Pair<String,ReqReply> pair = callbackStorage.get(correlationId);
        final ReqReply protoCommand = pair.getSecond();
        protoCommand.receive(pair.getFirst());
        callbackStorage.remove(correlationId);
    }


    private static class ReqReply {

        private long timeout;
        private String message = "";
        private boolean received = false;

        public String send(final String payload) {
            log.info("send C id "+payload);
            synchronized(payload) {
                if (!received) {
                    try {
                        payload.wait(timeout);
                    } catch (final InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        message = "Interrupted while waiting";
                        return message;
                    }
                }
                return (received) ? message : "Happened timeout : "+timeout;
            }
        }

        public void receive(final String receiverMessage) {
            log.info("Receive : {}",receiverMessage);
            received = true;
            message = receiverMessage;
            log.info("Notify  : "+receiverMessage);
            receiverMessage.notifyAll();
        }

        public void setTimeout(long timeout) {
            this.timeout = timeout;
        }
    }
}
