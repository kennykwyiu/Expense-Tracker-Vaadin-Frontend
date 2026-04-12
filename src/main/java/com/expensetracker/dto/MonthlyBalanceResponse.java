package com.expensetracker.dto;

import java.math.BigDecimal;

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
