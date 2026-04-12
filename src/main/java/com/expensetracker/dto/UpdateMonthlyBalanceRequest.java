package com.expensetracker.dto;

import java.math.BigDecimal;

public class UpdateMonthlyBalanceRequest {
    private BigDecimal lastMonthBalance;
    private BigDecimal incomeThisWeek;
    private BigDecimal expenseBudget;
}
