package ru.job4j.kafka.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainForReqReply {

    public static void main(final String[] args) {
        final ReqReply reply = new ReqReply(20);
        final String correlationId = UUID.randomUUID().toString();

        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> reply.send(correlationId));
        CompletableFuture.runAsync(
                () -> reply.receive(correlationId),
                CompletableFuture.delayedExecutor(1, TimeUnit.MILLISECONDS)
        );

        log.info("RESULT : "+task.join());
    }
}
