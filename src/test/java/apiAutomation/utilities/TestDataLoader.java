package apiAutomation.utilities;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class TestDataLoader {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private TestDataLoader() {
    }

    public static <T> T load(String resourcePath, Class<T> clazz) {
        try (InputStream stream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourcePath)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            return OBJECT_MAPPER.readValue(stream, clazz);
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to load test data from resource: " + resourcePath, ex);
        }
    }
}

