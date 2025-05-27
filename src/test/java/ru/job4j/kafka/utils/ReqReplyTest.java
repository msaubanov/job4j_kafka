package ru.job4j.kafka.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.job4j.kafka.service.ReqReplyService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

class ReqReplyTest {

    @Test
    @DisplayName("Test without timeout")
    void sendEverythingNormal() {
        final String correlationId = UUID.randomUUID().toString();
        final ReqReplyService service = new ReqReplyService(new ConcurrentHashMap<>());
        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> service.send(correlationId));
        CompletableFuture.runAsync(
                () -> service.receive(correlationId),
                CompletableFuture.delayedExecutor(1, TimeUnit.MILLISECONDS)
        );
        Assertions.assertEquals(correlationId, task.join());
    }

    @Test
    @DisplayName("With timeout")
    void sendWithTimeout() {
        final long delay = 2400;
        final long timeout = 2000;
        final String correlationId = UUID.randomUUID().toString();
        final ReqReplyService service = new ReqReplyService(new ConcurrentHashMap<>());
        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> service.send(correlationId));
        CompletableFuture.runAsync(
                () -> service.receive(correlationId),
                CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
        );
        final String expected = "Happened timeout : "+timeout;
        Assertions.assertEquals(expected,task.join());
    }

    @Test
    @DisplayName("With receiving wrong")
    void whenReceiveAnotherVal() {
        final String correlationId = "1";
        final ReqReplyService service = new ReqReplyService(new ConcurrentHashMap<>());
        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> service.send(correlationId));
        CompletableFuture.runAsync(
                () -> service.receive("2"),
                CompletableFuture.delayedExecutor(1000, TimeUnit.MILLISECONDS)
        );
        final String expected = "Happened timeout : "+2000;
        Assertions.assertEquals(expected,task.join());
    }
}