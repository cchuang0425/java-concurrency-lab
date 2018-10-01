package org.iii.math;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;

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
}
