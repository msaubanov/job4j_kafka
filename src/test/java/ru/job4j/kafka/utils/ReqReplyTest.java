package ru.job4j.kafka.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

class ReqReplyTest {

    //Доработайте код, приведённый выше, так, чтобы он работал с ключом correlationId:

//1 val correlationId = UUID.randomUUID().toString()
//2 val reply = ReqReply(10)
//3 val task = FutureTask({
//            reply.send(correlationId)
//    })
//    Добавьте тесты (три сценария):

//    send(correlationId = 1), receive(correlationId = 1)
//    send(correlationId = 1), таймаут
//    send(correlationId = 1), receive(correlationId = 2), таймаут
    final long timeout = 20;
    final ReqReply reply = new ReqReply(timeout);
    @Test
    @DisplayName("Test without timeout")
    void sendEverythingNormal() {

        final String correlationId = UUID.randomUUID().toString();

        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> reply.send(correlationId));
        CompletableFuture.runAsync(
                () -> reply.receive(correlationId),
                CompletableFuture.delayedExecutor(1, TimeUnit.MILLISECONDS)
        );
        Assertions.assertEquals(correlationId,task.join());
    }

    @Test
    @DisplayName("With timeout")
    void sendWithTimeout() {
        final long delay = 2000;
        final String correlationId = UUID.randomUUID().toString();

        final CompletableFuture<String> task = CompletableFuture.supplyAsync(()-> reply.send(correlationId));
        CompletableFuture.runAsync(
                () -> reply.receive(correlationId),
                CompletableFuture.delayedExecutor(delay, TimeUnit.MILLISECONDS)
        );
        final String expected = "Happened timeout : "+timeout;
        Assertions.assertEquals(expected,task.join());
    }

    @Test
    @DisplayName("With receiving wrong")
    void whenReceiveAnotherVal() {

    }
}