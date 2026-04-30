package com.expensetracker.service;

import com.expensetracker.dto.MonthlyBalanceResponse;
import com.expensetracker.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Service for calling Balance API endpoints
 */
@Service
public class BalanceService {

    private final Logger logger = new Logger(BalanceService.class);
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${backend.api.url:http://localhost:8080/api}")
    private String backendApiUrl;
    
    /**
     * Get monthly balance for user
     */
    public MonthlyBalanceResponse getMonthlyBalance(Integer year, Integer month) throws Exception {
        logger.info("Getting monthly balance for " + year + "-" + month);
        
        String url = backendApiUrl + "/balance/" + year + "/" + month;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 200) {
            logger.info("Balance retrieved successfully");
            return objectMapper.readValue(response.body(), MonthlyBalanceResponse.class);
        } else {
            logger.error("Failed to get balance. Status: " + response.statusCode());
            throw new Exception("Failed to get balance: " + response.statusCode());
        }
    }
    
    /**
     * Create monthly balance record
     */
    public MonthlyBalanceResponse createMonthlyBalance(Integer year, Integer month,
                                                       BigDecimal lastMonthBalance, BigDecimal expenseBudget) throws Exception {
        logger.info("Creating monthly balance for " + year + "-" + month);
        
        String url = backendApiUrl + "/balance?year=" + year + "&month=" + month +
                "&lastMonthBalance=" + (lastMonthBalance != null ? lastMonthBalance : 0) +
                "&expenseBudget=" + (expenseBudget != null ? expenseBudget : 0);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.noBody())
                .header("Content-Type", "application/json")
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() == 201) {
            logger.info("Balance created successfully");
            return objectMapper.readValue(response.body(), MonthlyBalanceResponse.class);
        } else {
            logger.error("Failed to create balance. Status: " + response.statusCode());
            throw new Exception("Failed to create balance: " + response.statusCode());
        }
    }

    public MonthlyBalanceResponse updateIncomeThisWeek(Integer year, Integer month, BigDecimal income) throws Exception {
        logger.info("Updating income for " + year + "-" + month + ": " + income);

    }
}
