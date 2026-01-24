package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for listing expenses from the backend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListExpensesResponse {
    private List<ExpenseResponse> expenses;
    private BigDecimal total;
    private Integer count;
    private Integer year;
    private Integer month;
}
