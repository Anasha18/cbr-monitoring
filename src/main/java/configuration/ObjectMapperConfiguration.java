package configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ObjectMapperConfiguration {
    private static ObjectMapper mapper;

    private ObjectMapperConfiguration() {
    }

    public static ObjectMapper initJackson() {
        if (mapper != null) return mapper;

        var objectMapper = new ObjectMapper();

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.findAndRegisterModules();

        mapper = objectMapper;

        return mapper;
    }
}
