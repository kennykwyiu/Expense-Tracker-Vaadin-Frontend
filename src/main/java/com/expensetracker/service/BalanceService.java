package com.expensetracker.service;

import com.expensetracker.dto.MonthlyBalanceResponse;
import com.expensetracker.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpClient;

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

    }
}
