package org.iii.mq;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import org.iii.CommandWorkerConfig;
import org.iii.common.CommandMessage;
import org.iii.common.ResultMessage;
import org.iii.common.Results;

import static org.iii.CommandWorkerConfig.WORKER_POOL_NAME;
import static org.iii.util.JsonUtils.convertJsonFromObject;

public class CommandExecutors {

    @Async(WORKER_POOL_NAME)
    public static CompletableFuture<ResultMessage<?>> executeCommand(CommandMessage<?> commandMessage) {
        return CompletableFuture.supplyAsync(() -> sendCommandAndWait(commandMessage));
    }

    private static ResultMessage<?> sendCommandAndWait(CommandMessage<?> commandMessage) {
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        ChannelRegistry channels = context.getBean(ChannelRegistry.class);
        CommandSender sender = context.getBean(CommandSender.class);

        try {
            LinkedBlockingQueue<String> channel = channels.createChannel(commandMessage.getId());
            sender.sendCommand(convertJsonFromObject(commandMessage));
            String resultJson = channel.take();

            return Results.convertResult(resultJson);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace(System.err);
            throw new RuntimeException(ex);
        }
    }
}
