package com.twinline.loan_management_system.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twinline.loan_management_system.entity.Applicant;
import com.twinline.loan_management_system.entity.Workflow;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long> {

    List<Workflow> findByApplicantApplicantIdOrderByWorkflowStepStepOrderAsc(Long applicantId);

//    @Query("SELECT w FROM Workflow w WHERE w.status = :status AND w.workflowStep.approver.userId = :approverId")
//    List<Workflow> findByStatusAndWorkflowStepApproverId(@Param("status") String status,
//                                                         @Param("approverId") Long approverId);
    
    Optional<Workflow> findByApplicantApplicantIdAndStatus(Long applicantId, String status);

}
