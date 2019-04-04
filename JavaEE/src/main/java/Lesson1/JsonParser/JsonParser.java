package Lesson1.JsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

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

        } catch (IOException e) {
            throw new IOException("Can't parse JSON to Object");
        }
    }
}
