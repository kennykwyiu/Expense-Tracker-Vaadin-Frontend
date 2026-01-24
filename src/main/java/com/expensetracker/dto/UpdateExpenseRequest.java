package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for updating an expense.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExpenseRequest {
    private LocalDate date;
    private BigDecimal amount;
    private String category;
    private String description;
}
