package com.ControlSeguimiento.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.StreamWriteConstraints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        // Crear el ObjectMapper por defecto
        ObjectMapper objectMapper = new ObjectMapper();

        // Crear un JsonFactory con StreamWriteConstraints personalizados
        JsonFactory jsonFactory = objectMapper.getFactory();

        // Establecer las restricciones de escritura de flujo
        StreamWriteConstraints constraints = StreamWriteConstraints.builder()
                .maxNestingDepth(2000) // Ajusta este valor según tus necesidades
                .build();
        jsonFactory.setStreamWriteConstraints(constraints);

        // Devolver el ObjectMapper personalizado
        return objectMapper;
    }
}
