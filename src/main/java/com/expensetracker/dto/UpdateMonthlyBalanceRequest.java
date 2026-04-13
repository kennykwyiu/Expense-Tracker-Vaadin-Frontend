package com.expensetracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
/**
 * DTO for updating Monthly Balance via API.
 * Used to send balance update requests to the backend.
 */
@Setter
@Getter
public class UpdateMonthlyBalanceRequest {
    private BigDecimal lastMonthBalance;
    private BigDecimal incomeThisWeek;
    private BigDecimal expenseBudget;

    /**
     * Default constructor.
     */
    public UpdateMonthlyBalanceRequest() {
    }

    /**
     * Constructor with all fields.
     */
    public UpdateMonthlyBalanceRequest(BigDecimal lastMonthBalance,
                                       BigDecimal incomeThisWeek,
                                       BigDecimal expenseBudget) {
        this.lastMonthBalance = lastMonthBalance;
        this.incomeThisWeek = incomeThisWeek;
        this.expenseBudget = expenseBudget;
    }

    // Getters and Setters

    @Override
    public String toString() {
        return "UpdateMonthlyBalanceRequest{" +
                "lastMonthBalance=" + lastMonthBalance +
                ", incomeThisWeek=" + incomeThisWeek +
                ", expenseBudget=" + expenseBudget +
                '}';
    }
}
