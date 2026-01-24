package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO for a single expense from the backend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {
    private Integer id;
    private LocalDate date;
    private BigDecimal amount;
    private String category;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
