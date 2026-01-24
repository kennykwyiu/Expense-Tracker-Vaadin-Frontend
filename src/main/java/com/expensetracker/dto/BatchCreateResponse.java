package com.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Response DTO for batch create operation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BatchCreateResponse {
    private List<ExpenseResponse> created;
    private List<BatchFailure> failed;
    private Integer totalCreated;

    /**
     * Inner class representing a failed expense in batch operation.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchFailure {
        private Integer index;
        private String error;
    }
}
