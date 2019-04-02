package valoon.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import valoon.exceptions.JacksonJsonMappingException;

import java.io.IOException;
import java.util.TimeZone;

public class JacksonJsonMapper {

    private ObjectMapper mapper;

    public JacksonJsonMapper() {
        this.mapper = new ObjectMapper();
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        this.mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        this.mapper.setTimeZone(TimeZone.getDefault());
    }

    public <T> T deserialize(String jsonString, Class<T> clazz) {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonParseException | JsonMappingException ex) {
            String message = String.format("Error de-serializing json %s into [%s] instance.", jsonString, clazz.getCanonicalName());
            System.out.println(message);
            throw new JacksonJsonMappingException(message);
        } catch (IOException ex) {
            String message = String.format("Fatal IOException parsing json %s into [%s] instance", jsonString, clazz.getCanonicalName());
            throw new JacksonJsonMappingException(message);
        }
    }

    public String serialize(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException ex) {
            String message = String.format("Error serializing object %s.", object);
            System.out.println(message);
            throw new JacksonJsonMappingException(message);
        }
    }
}
