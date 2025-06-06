package ru.job4j.kafka.utils;

import lombok.extern.slf4j.Slf4j;
import ru.job4j.kafka.service.ReqReplyService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainForReqReply {

    public static void main(final String[] args) {
        final ReqReplyService service =  new ReqReplyService(2000);
        final String correlationId = UUID.randomUUID().toString();

        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> service.send(correlationId, correlationId));
        CompletableFuture.runAsync(
                () -> service.receive(correlationId),
                CompletableFuture.delayedExecutor(1, TimeUnit.MILLISECONDS)
        );

        log.info("RESULT : "+task.join());
    }
}
