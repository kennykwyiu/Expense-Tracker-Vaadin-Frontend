package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * HTTP Client Service for communicating with the Spring Boot backend.
 * Handles all REST API calls for expense operations.
 */
@Service
public class ApiClient {
    private static final Logger logger = new Logger(ApiClient.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ApiClient(
            @Value("${backend.api.url}") String backendApiUrl,
            WebClient.Builder webClientBuilder,
            ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(backendApiUrl).build();
        this.objectMapper = objectMapper;
    }

    /**
     * Create a single expense.
     */
    public ExpenseResponse createExpense(LocalDate date, BigDecimal amount, String category, String description) {
        try {
            CreateExpenseRequest request = new CreateExpenseRequest();
            request.setDate(date);
            request.setAmount(amount);
            request.setCategory(category);
            request.setDescription(description);

            logger.info("Creating expense: " + category + " - " + amount);

            return webClient.post()
                    .uri("/expenses")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ExpenseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Failed to create expense: " + e.getMessage());
            throw new RuntimeException("Failed to create expense: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error creating expense: " + e.getMessage());
            throw new RuntimeException("Error creating expense: " + e.getMessage());
        }
    }

}
