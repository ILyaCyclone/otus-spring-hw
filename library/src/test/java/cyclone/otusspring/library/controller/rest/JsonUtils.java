package cyclone.otusspring.library.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

public class JsonUtils {
    private JsonUtils() {
    }

    public static <T> T readValueFromMvcResult(ObjectMapper objectMapper, MvcResult mvcResult, Class<T> clazz) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(content, clazz);
    }

    public static <T> List<T> readListFromMvcResult(ObjectMapper objectMapper, MvcResult mvcResult, Class<T> clazz) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        ObjectReader reader = objectMapper.readerFor(clazz);
        return reader.<T>readValues(content).readAll();
    }

    public static <T> String writeValue(ObjectMapper objectMapper, T object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}