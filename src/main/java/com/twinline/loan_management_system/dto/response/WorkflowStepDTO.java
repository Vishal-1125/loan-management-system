package com.twinline.loan_management_system.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkflowStepDTO {
    private String stepName;
    private String status;
    private String comments;
    private LocalDateTime updatedAt;
    private boolean isCurrent;
}

