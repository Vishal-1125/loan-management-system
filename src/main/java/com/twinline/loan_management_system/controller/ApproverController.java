package com.twinline.loan_management_system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twinline.loan_management_system.dto.request.ApplicationReqDto;
import com.twinline.loan_management_system.dto.request.ApplicationWorkflowReqDto;
import com.twinline.loan_management_system.dto.request.ClaimApplicationReqDto;
import com.twinline.loan_management_system.dto.request.StepUpdateReqDto;
import com.twinline.loan_management_system.dto.response.ApplicantResDto;
import com.twinline.loan_management_system.dto.response.WorkflowStepDTO;
import com.twinline.loan_management_system.entity.Applicant;
import com.twinline.loan_management_system.entity.Workflow;
import com.twinline.loan_management_system.service.ApplicantService;
import com.twinline.loan_management_system.service.WorkflowService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/approver")
public class ApproverController {

    private final ApplicantService applicantService;
    private final WorkflowService workflowService;
    private final HttpSession session;

    public ApproverController(ApplicantService applicantService, WorkflowService workflowService, HttpSession session) {
        this.applicantService = applicantService;
        this.workflowService = workflowService;
        this.session = session;
    }

    @PostMapping("/applications/")
    public List<ApplicantResDto> getApplications(@RequestBody ApplicationReqDto applicationReqDto) {
        Long approverId = (Long) session.getAttribute("userId");
        applicationReqDto.setApproverId(approverId);

        List<Applicant> applicants = applicantService.getApplications(applicationReqDto);
        List<ApplicantResDto> applicantResDtos = new ArrayList<>();

        applicants.forEach(applicant -> {
            ApplicantResDto dto = new ApplicantResDto();
            BeanUtils.copyProperties(applicant, dto); 
            applicantResDtos.add(dto);
        });

        return applicantResDtos;
    }


    @PostMapping("/applications/claim")
    public Applicant claimApplication(@RequestBody ClaimApplicationReqDto claimApplicationReqDto) {
        Long approverId = (Long) session.getAttribute("userId");
        claimApplicationReqDto.setApproverId(approverId);
        return applicantService.claimApplicant(claimApplicationReqDto);
    }

    @PostMapping("/applications/approve")
    public Workflow approveStep(@RequestBody StepUpdateReqDto stepUpdateReqDto) {
        Long approverId = (Long) session.getAttribute("userId");
        stepUpdateReqDto.setApproverId(approverId);
        return workflowService.approveStep(stepUpdateReqDto);
    }

    @PostMapping("/applications/reject")
    public Workflow rejectStep(@RequestBody StepUpdateReqDto stepUpdateReqDto) {
        Long approverId = (Long) session.getAttribute("userId");
        stepUpdateReqDto.setApproverId(approverId);
        return workflowService.rejectStep(stepUpdateReqDto);
    }
    
    

    @PostMapping("/applications/workflows")
    public List<WorkflowStepDTO> getApplicationWorkflow(@RequestBody ApplicationWorkflowReqDto applicationWorkflowReqDto) {
        return workflowService.getWorkflowsByApplicant(applicationWorkflowReqDto);
    }
}
