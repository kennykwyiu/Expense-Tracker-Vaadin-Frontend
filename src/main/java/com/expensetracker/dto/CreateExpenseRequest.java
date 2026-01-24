package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating a single expense.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateExpenseRequest {
    private LocalDate date;
    private BigDecimal amount;
    private String category;
    private String description;
}
