package org.iii.common;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import org.springframework.context.ApplicationContext;

import org.iii.CommandWorkerConfig;
import org.iii.fib.FibCommand;
import org.iii.mq.ChannelRegistry;
import org.iii.prime.PrimeCommand;

import static org.iii.util.JsonUtils.convertObjectFromJson;
import static org.iii.util.JsonUtils.jsonMapper;

public class Results {

    private static final Map<String, Class<?>> COMMAND_RESULT_TYPES;

    static {
        ImmutableMap.Builder<String, Class<?>> resultBuilder = ImmutableMap.builder();
        COMMAND_RESULT_TYPES = resultBuilder.put(FibCommand.COMMAND_NAME, Long.class)
                                            .put(PrimeCommand.COMMAND_NAME, Boolean.class)
                                            .build();
    }

    public static ResultMessage<?> convertResult(String jsonMessage) throws IOException {
        try {
            JsonNode json = jsonMapper().readTree(jsonMessage);
            UUID commandId = UUID.fromString(json.get("id").asText());
            String commandName = json.get("name").asText();
            Class<?> resultType = COMMAND_RESULT_TYPES.get(commandName);
            return ResultMessage.builder()
                                .id(commandId)
                                .name(commandName)
                                .result(convertObjectFromJson(json.get("result").toString(), resultType))
                                .build();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
            throw ex;
        }
    }

    public static void pipeToCommand(String jsonMessage) {
        ApplicationContext context =
                CommandWorkerConfig.ApplicationContextProvider.getApplicationContext();

        ChannelRegistry channels = context.getBean(ChannelRegistry.class);

        try {
            JsonNode json = jsonMapper().readTree(jsonMessage);
            UUID id = UUID.fromString(json.get("id").asText());
            channels.findChannel(id)
                    .ifPresent(channel -> channel.offer(jsonMessage));
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }
}
