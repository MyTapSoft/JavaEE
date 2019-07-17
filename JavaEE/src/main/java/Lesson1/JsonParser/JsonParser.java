package Lesson1.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonParser<T> {
    public T jsonToObject(HttpServletRequest req, Class<T> objClass) throws IOException {
        try (BufferedReader reader = req.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return new ObjectMapper().readValue(sb.toString(), objClass);

        }
    }

    public String objectToJson(T object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    public List<String> objectToJson(List<T> objects) throws JsonProcessingException {
        List<String> result = new ArrayList<>();
        for (T object : objects) {
            result.add(objectToJson(object));
        }
        return result;
    }
}
