package com.expensetracker.dto;

import java.math.BigDecimal;
/**
 * DTO for updating Monthly Balance via API.
 * Used to send balance update requests to the backend.
 */
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

    public BigDecimal getLastMonthBalance() {
        return lastMonthBalance;
    }

    public void setLastMonthBalance(BigDecimal lastMonthBalance) {
        this.lastMonthBalance = lastMonthBalance;
    }

    public BigDecimal getIncomeThisWeek() {
        return incomeThisWeek;
    }

    public void setIncomeThisWeek(BigDecimal incomeThisWeek) {
        this.incomeThisWeek = incomeThisWeek;
    }

    public BigDecimal getExpenseBudget() {
        return expenseBudget;
    }

    public void setExpenseBudget(BigDecimal expenseBudget) {
        this.expenseBudget = expenseBudget;
    }

    @Override
    public String toString() {
        return "UpdateMonthlyBalanceRequest{" +
                "lastMonthBalance=" + lastMonthBalance +
                ", incomeThisWeek=" + incomeThisWeek +
                ", expenseBudget=" + expenseBudget +
                '}';
    }
}
