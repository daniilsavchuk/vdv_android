package com.its.vdv.rest.mappers;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import static org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES;

public class JsonMapper extends MappingJacksonHttpMessageConverter {
    public JsonMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        setObjectMapper(objectMapper);
    }
}
