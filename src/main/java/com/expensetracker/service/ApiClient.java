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

    /**
     * Update an existing expense.
     */
    public ExpenseResponse updateExpense(Integer id, LocalDate date, BigDecimal amount, String category, String description) {
        try {
            UpdateExpenseRequest request = new UpdateExpenseRequest();
            request.setDate(date);
            request.setAmount(amount);
            request.setCategory(category);
            request.setDescription(description);

            logger.info("Updating expense ID: " + id);

            return webClient.put()
                    .uri("/expenses/{id}", id)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ExpenseResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Failed to update expense: " + e.getMessage());
            throw new RuntimeException("Failed to update expense: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error updating expense: " + e.getMessage());
            throw new RuntimeException("Error updating expense: " + e.getMessage());
        }
    }

    /**
     * Delete an expense by ID.
     */
    public void deleteExpense(Integer id) {
        try {
            logger.info("Deleting expense ID: " + id);

            webClient.delete()
                    .uri("/expenses/{id}", id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Failed to delete expense: " + e.getMessage());
            throw new RuntimeException("Failed to delete expense: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error deleting expense: " + e.getMessage());
            throw new RuntimeException("Error deleting expense: " + e.getMessage());
        }
    }

    /**
     * List expenses for a specific month and year.
     */
    public ListExpensesResponse listExpenses(Integer year, Integer month) {
        try {
            logger.info("Fetching expenses for " + year + "-" + month);

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/expenses")
                            .queryParam("year", year)
                            .queryParam("month", month)
                            .build())
                    .retrieve()
                    .bodyToMono(ListExpensesResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Failed to list expenses: " + e.getMessage());
            throw new RuntimeException("Failed to list expenses: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error listing expenses: " + e.getMessage());
            throw new RuntimeException("Error listing expenses: " + e.getMessage());
        }
    }

    /**
     * Batch create multiple expenses.
     */
    public BatchCreateResponse batchCreateExpenses(List<CreateExpenseRequest> expenses) {
        try {
            BatchCreateExpensesRequest request = new BatchCreateExpensesRequest();
            request.setExpenses(expenses);

            logger.info("Batch creating " + expenses.size() + " expenses");

            return webClient.post()
                    .uri("/expenses/batch")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(BatchCreateResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Failed to batch create expenses: " + e.getMessage());
            throw new RuntimeException("Failed to batch create expenses: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            logger.error("Error batch creating expenses: " + e.getMessage());
            throw new RuntimeException("Error batch creating expenses: " + e.getMessage());
        }
    }
}
