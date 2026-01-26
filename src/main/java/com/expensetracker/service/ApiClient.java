package com.expensetracker.service;

import com.expensetracker.util.Logger;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * HTTP Client Service for communicating with the Spring Boot backend.
 * Handles all REST API calls for expense operations.
 */
@Service
public class ApiClient {
    private static final Logger logger = new Logger(ApiClient.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

}
