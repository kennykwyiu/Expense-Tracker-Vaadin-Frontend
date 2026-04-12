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

    /**
     * Default constructor.
     */
    public MonthlyBalanceResponse() {
    }

    /**
     * Constructor with all fields.
     */
    public MonthlyBalanceResponse(Integer id, Integer userId, Integer year, Integer month,
                                  BigDecimal lastMonthBalance, BigDecimal incomeThisWeek,
                                  BigDecimal expenseBudget, BigDecimal currentBalance,
                                  String createdAt, String updatedAt) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.lastMonthBalance = lastMonthBalance;
        this.incomeThisWeek = incomeThisWeek;
        this.expenseBudget = expenseBudget;
        this.currentBalance = currentBalance;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "MonthlyBalanceResponse{" +
                "id=" + id +
                ", year=" + year +
                ", month=" + month +
                ", lastMonthBalance=" + lastMonthBalance +
                ", incomeThisWeek=" + incomeThisWeek +
                ", expenseBudget=" + expenseBudget +
                ", currentBalance=" + currentBalance +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
