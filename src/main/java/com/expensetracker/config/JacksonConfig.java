package com.expensetracker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Jackson configuration for Java 8 date/time support.
 * Enables proper serialization and deserialization of LocalDateTime, LocalDate, etc.
 */
@Configuration
public class JacksonConfig {
    /**
     * Configure ObjectMapper to support Java 8 date/time types.
     * This bean ensures that Jackson can properly handle LocalDateTime, LocalDate, LocalTime, etc.
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register the JavaTimeModule to handle Java 8 date/time types
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
