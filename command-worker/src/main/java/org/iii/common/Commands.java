package org.iii.common;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;

import org.iii.CommandWorkerConfig;
import org.iii.fib.FibCommand;
import org.iii.prime.PrimeCommand;

import static org.iii.CommandWorkerConfig.WORKER_POOL_NAME;
import static org.iii.util.JsonUtils.jsonMapper;

public abstract class Commands {
    private Commands() {}

    private static final Map<String, Class<?>> COMMAND_PARAMETER_TYPES;

    static {
        ImmutableMap.Builder<String, Class<?>> paramBuilder = ImmutableMap.builder();
        COMMAND_PARAMETER_TYPES = paramBuilder.put(FibCommand.COMMAND_NAME, Long.class)
                                              .put(PrimeCommand.COMMAND_NAME, Long.class)
                                              .build();
    }

    public static CommandMessage<?> convertCommandMessage(String jsonMessage) throws IOException {
        try {
            JsonNode json = jsonMapper().readTree(jsonMessage);
            UUID commandId = UUID.fromString(json.get("id").asText());
            String commandName = json.get("name").asText();
            Class<?> paramType = COMMAND_PARAMETER_TYPES.get(commandName);
            return CommandMessage.builder()
                                 .id(commandId)
                                 .name(commandName)
                                 .param(jsonMapper().readValue(json.get("param").toString(), paramType))
                                 .build();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw ex;
        }
    }

    @Async(WORKER_POOL_NAME)
    @SuppressWarnings("unchecked")
    public static <R, P> CompletableFuture<ResultMessage<R>> runCommandAsync(CommandMessage<P> commandMessage) {
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        Command<R, P> command = (Command<R, P>) context.getBean(commandMessage.getName());

        return CompletableFuture.supplyAsync(
                () -> ResultMessage.<R>builder().id(commandMessage.getId())
                                                .name(commandMessage.getName())
                                                .result(command.execute(commandMessage.getParam()))
                                                .build());
    }

    @SuppressWarnings("unchecked")
    public static <R, P> ResultMessage<R> runCommandSync(CommandMessage<P> commandMessage) {
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        Command<R, P> command = (Command<R, P>) context.getBean(commandMessage.getName());

        return ResultMessage.<R>builder().id(commandMessage.getId())
                                         .name(commandMessage.getName())
                                         .result(command.execute(commandMessage.getParam()))
                                         .build();
    }
}
