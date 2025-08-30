package com.twinline.loan_management_system.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "workflow", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"applicant_id", "workflow_step_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workflowId;

    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;

    @ManyToOne
    @JoinColumn(name = "workflow_step_id", nullable = false)
    private WorkflowMaster workflowStep;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(nullable = false)
    private String status = "Pending";

    private String comments;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
