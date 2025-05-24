package ru.job4j.kafka.service;


import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import ru.job4j.kafka.utils.ReqReply;

import java.util.Map;

@Slf4j
public class ReqReplyService {

    private final Map<String, Pair<String,ReqReply> > callbackStorage;

    public ReqReplyService(final Map<String, Pair<String,ReqReply>> callbackStorage) {
        this.callbackStorage = callbackStorage;
    }

    public String send(final String correlationId, final ReqReply reqReply) {
        final String response ;
        callbackStorage.put(correlationId, new Pair<>(correlationId,reqReply) );
        response = reqReply.send(correlationId);
        return response;
    }

    public void receive (final String correlationId) {
        final Pair<String,ReqReply> pair = callbackStorage.get(correlationId);
        final ReqReply protoCommand = pair.getSecond();
        protoCommand.receive(pair.getFirst());
        callbackStorage.remove(correlationId);
    }
}
