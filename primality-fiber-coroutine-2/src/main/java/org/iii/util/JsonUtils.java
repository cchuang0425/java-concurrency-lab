package org.iii.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtils {
    private JsonUtils() {}

    public static ObjectMapper jsonMapper(){
        return new ObjectMapper().findAndRegisterModules();
    }

    public static String convertJsonFromObject(Object model) throws JsonProcessingException {
        return jsonMapper().writeValueAsString(model);
    }

    public static <T> T convertObjectFromJson(String json, Class<T> type) throws IOException {
        return jsonMapper().readValue(json, type);
    }
}
