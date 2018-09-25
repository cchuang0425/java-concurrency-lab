package org.iii.mq;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import org.iii.CommandWorkerConfig;
import org.iii.common.CommandMessage;
import org.iii.common.ResultMessage;
import org.iii.common.Results;

import static org.iii.util.JsonUtils.convertToJson;

public class CommandExecutors {


    @Async("workerPool")
    public static CompletableFuture<ResultMessage<?>> executeCommand(CommandMessage<?> commandMessage) {
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        Executor pool = (Executor) context.getBean("workerPool");

        return CompletableFuture.supplyAsync(() -> sendCommandAndWait(commandMessage), pool);
    }

    private static ResultMessage<?> sendCommandAndWait(CommandMessage<?> commandMessage) {
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        ChannelRegistry channels = context.getBean(ChannelRegistry.class);
        CommandSender sender = context.getBean(CommandSender.class);

        try {
            LinkedBlockingQueue<String> channel = channels.createChannel(commandMessage.getId());
            sender.sendCommand(convertToJson(commandMessage));
            String resultJson = channel.take();

            return Results.convertResult(resultJson);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
