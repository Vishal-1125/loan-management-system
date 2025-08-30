package com.twinline.loan_management_system.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workflow_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workflowStepId;

    @Column(nullable = false)
    private String stepName;

    @Column(nullable = false)
    private Integer stepOrder;

    private String description;
}
