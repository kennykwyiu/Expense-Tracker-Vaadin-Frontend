package com.expensetracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring Boot application entry point for Vaadin frontend.
 */
@SpringBootApplication
public class ExpenseTrackerFrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTrackerFrontendApplication.class, args);
    }

    /**
     * Create WebClient bean for HTTP requests.
     */
    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
