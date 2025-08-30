package com.twinline.loan_management_system.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twinline.loan_management_system.entity.WorkflowMaster;

@Repository
public interface WorkflowMasterRepository extends JpaRepository<WorkflowMaster, Long> {

    List<WorkflowMaster> findAllByOrderByStepOrderAsc();

    WorkflowMaster findByStepOrder(Integer stepOreder);
    
//    WorkflowMaster findFirstByStepOrderGreaterThanOrderByStepOrderAsc(Integer stepOrder);
}
