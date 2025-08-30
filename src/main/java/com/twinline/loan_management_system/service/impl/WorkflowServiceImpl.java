package com.twinline.loan_management_system.service.impl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.twinline.loan_management_system.dto.request.ApplicationWorkflowReqDto;
import com.twinline.loan_management_system.dto.request.StepUpdateReqDto;
import com.twinline.loan_management_system.dto.response.WorkflowStepDTO;
import com.twinline.loan_management_system.entity.Applicant;
import com.twinline.loan_management_system.entity.User;
import com.twinline.loan_management_system.entity.Workflow;
import com.twinline.loan_management_system.entity.WorkflowMaster;
import com.twinline.loan_management_system.repo.ApplicantRepository;
import com.twinline.loan_management_system.repo.WorkflowMasterRepository;
import com.twinline.loan_management_system.repo.WorkflowRepository;
import com.twinline.loan_management_system.service.WorkflowService;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    private final WorkflowRepository workflowRepository;
    private final WorkflowMasterRepository workflowMasterRepository;
    private final ApplicantRepository applicantRepository;
    
    

    public WorkflowServiceImpl(WorkflowRepository workflowRepository, ApplicantRepository applicantRepository,WorkflowMasterRepository workflowMasterRepository) {
		super();
		this.workflowRepository = workflowRepository;
		this.workflowMasterRepository = workflowMasterRepository;
		this.applicantRepository = applicantRepository;
	}

	@Override
    public Workflow approveStep(StepUpdateReqDto stepUpdateReqDto) {
		Applicant applicant = applicantRepository.findById(stepUpdateReqDto.getApplicantId())
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        if (applicant.getClaimedBy() == null || !applicant.getClaimedBy().getUserId().equals(stepUpdateReqDto.getApproverId())) {
            throw new RuntimeException("You are not authorized to act on this applicant");
        }

        Workflow workflow = workflowRepository.findByApplicantApplicantIdAndStatus(stepUpdateReqDto.getApplicantId(), "Pending")
                .orElseThrow(() -> new RuntimeException("No pending workflow step found"));

        workflow.setApprover(User.builder().userId(stepUpdateReqDto.getApproverId()).build());
        workflow.setComments(stepUpdateReqDto.getComments());
        workflow.setStatus("Approved");
        workflow.setUpdatedAt(LocalDateTime.now());
        workflowRepository.save(workflow);
        
        WorkflowMaster step= workflowMasterRepository.findByStepOrder(workflow.getWorkflowStep().getStepOrder()+1);
        if(step==null) {
        	   applicant.setStatus("Approved");
               
        }
        else {
        	Workflow NextWorkflow= Workflow.builder().applicant(applicant).workflowStep(step).status("Pending").createdAt(LocalDateTime.now()).build();
        	workflowRepository.save(NextWorkflow);
        	applicant.setStatus("In Progress");
        }

        applicant.setUpdatedAt(LocalDateTime.now());
        applicantRepository.save(applicant);
        

        return workflow;
    }

    @Override
    public Workflow rejectStep(StepUpdateReqDto stepUpdateReqDto) {
    	 Applicant applicant = applicantRepository.findById(stepUpdateReqDto.getApplicantId())
                 .orElseThrow(() -> new RuntimeException("Applicant not found"));

         if (applicant.getClaimedBy() == null || !applicant.getClaimedBy().getUserId().equals(stepUpdateReqDto.getApproverId())) {
             throw new RuntimeException("You are not authorized to act on this applicant");
         }

         Workflow workflow = workflowRepository.findByApplicantApplicantIdAndStatus(stepUpdateReqDto.getApplicantId(), "Pending")
                 .orElseThrow(() -> new RuntimeException("No pending workflow step found"));

         workflow.setApprover(User.builder().userId(stepUpdateReqDto.getApproverId()).build());
         workflow.setComments(stepUpdateReqDto.getComments());
         workflow.setStatus("Rejected");
         workflow.setUpdatedAt(LocalDateTime.now());
         workflowRepository.save(workflow);

         applicant.setStatus("Rejected"); 
         applicant.setUpdatedAt(LocalDateTime.now());
         applicantRepository.save(applicant);

         return workflow;
    }

    @Override
    public List<WorkflowStepDTO> getWorkflowsByApplicant(ApplicationWorkflowReqDto applicationWorkflowReqDto) {
    
    	Applicant applicant = applicantRepository.findById(applicationWorkflowReqDto.getApplicationId())
                .orElseThrow(() -> new RuntimeException("Applicant not found"));
    	List<Workflow> steps=workflowRepository.findByApplicantApplicantIdOrderByWorkflowStepStepOrderAsc(applicant.getApplicantId());

    	List<WorkflowStepDTO> workflowStepsDto = new ArrayList<>();
    	for (Workflow w : steps) {
    	    WorkflowStepDTO dto = new WorkflowStepDTO();
    	    dto.setStepName(w.getWorkflowStep().getStepName());
    	    dto.setStatus(w.getStatus());
    	    dto.setComments(w.getComments());
    	    dto.setUpdatedAt(w.getUpdatedAt());
    	    dto.setCurrent("Pending".equals(w.getStatus()));
    	    workflowStepsDto.add(dto);
    	}

    	return workflowStepsDto;

    }

  
}
