package org.iii.math;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import co.paralleluniverse.strands.channels.Channel;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

import org.iii.common.ChannelRegistry;
import org.iii.domain.ResultMessage;

import static org.iii.util.JsonUtils.convertObjectFromJson;
import static org.iii.util.JsonUtils.jsonMapper;

public class MathResults {

    private static final Map<String, Class<?>> COMMAND_RESULT_TYPES;

    static {
        ImmutableMap.Builder<String, Class<?>> resultBuilder = ImmutableMap.builder();
        COMMAND_RESULT_TYPES = resultBuilder.put(MathCommands.FIB_COMMAND, Long.class)
                                            .put(MathCommands.PRIME_COMMAND, Boolean.class)
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

    public static void resumeToCommand(ResultMessage<?> resultMessage) {
        switch (resultMessage.getName()) {
            case MathCommands.FIB_COMMAND:
                resume((ResultMessage<Long>) resultMessage, Long.class);
                break;
            case MathCommands.PRIME_COMMAND:
                resume((ResultMessage<Boolean>) resultMessage, Boolean.class);
                break;
            default:
        }
    }

    @Suspendable
    private static <R> void resume(ResultMessage<R> trueResult, Class<R> resultType) {
        ChannelRegistry channels = ChannelRegistry.getInstance();

        Optional<Channel<R>> maybe = channels.find(trueResult.getId(), resultType);
        if (maybe.isPresent()) {
            try (Channel<R> channel = maybe.get()) {
                channel.send(trueResult.getResult());
            } catch (InterruptedException | SuspendExecution ex) {
                throw new AssertionError(ex);
            }
        }
    }
}
