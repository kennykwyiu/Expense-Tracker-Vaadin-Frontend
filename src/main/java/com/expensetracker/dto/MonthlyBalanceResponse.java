package com.expensetracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DTO for Monthly Balance API responses.
 * Represents the monthly balance data returned from the backend.
 */
@Getter
@Setter
public class MonthlyBalanceResponse {
    private Integer id;
    private Integer userId;
    private Integer year;
    private Integer month;
    private BigDecimal lastMonthBalance;
    private BigDecimal incomeThisWeek;
    private BigDecimal expenseBudget;
    private BigDecimal currentBalance;
    private String createdAt;
    private String updatedAt;

}
