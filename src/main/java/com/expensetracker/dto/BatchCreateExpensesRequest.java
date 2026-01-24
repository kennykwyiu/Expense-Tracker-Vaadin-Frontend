package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for batch creating expenses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchCreateExpensesRequest {
    private List<CreateExpenseRequest> expenses;
}
