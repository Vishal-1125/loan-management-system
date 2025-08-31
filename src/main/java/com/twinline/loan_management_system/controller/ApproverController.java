package com.twinline.loan_management_system.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twinline.loan_management_system.dto.request.ApplicationReqDto;
import com.twinline.loan_management_system.dto.request.ApplicationWorkflowReqDto;
import com.twinline.loan_management_system.dto.request.ClaimApplicationReqDto;
import com.twinline.loan_management_system.dto.request.StepUpdateReqDto;
import com.twinline.loan_management_system.dto.response.ApplicantResDto;
import com.twinline.loan_management_system.dto.response.ResponseDataDto;
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
	public ResponseEntity<ResponseDataDto<List<ApplicantResDto>>> getApplications(
			@RequestBody ApplicationReqDto applicationReqDto) {

		Long approverId = (Long) session.getAttribute("userId");
		applicationReqDto.setApproverId(approverId);

		List<Applicant> applicants = applicantService.getApplications(applicationReqDto);

		List<ApplicantResDto> applicantResDtos = applicants.stream().map(applicant -> {
			ApplicantResDto dto = new ApplicantResDto();
			BeanUtils.copyProperties(applicant, dto);
			return dto;
		}).toList();

		ResponseDataDto<List<ApplicantResDto>> response = new ResponseDataDto<>();
		response.setData(applicantResDtos);
		response.setMessage(applicantResDtos.isEmpty() ? "No applications found" : "Data Fetch successfully");
		response.setStatus("0");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/applications/claim")
	public ResponseEntity<ResponseDataDto<ApplicantResDto>> claimApplication(
			@RequestBody ClaimApplicationReqDto claimApplicationReqDto) {
		Long approverId = (Long) session.getAttribute("userId");
		claimApplicationReqDto.setApproverId(approverId);
		
		ApplicantResDto applicantResDto = applicantService.claimApplicant(claimApplicationReqDto);
		
		ResponseDataDto<ApplicantResDto> response = new ResponseDataDto<>();
		
		response.setData(applicantResDto);
		response.setMessage("Application Cliamed successfully");
		response.setStatus("0");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/applications/approve")
	public ResponseEntity<ResponseDataDto<Workflow>> approveStep(@RequestBody StepUpdateReqDto stepUpdateReqDto) {
		Long approverId = (Long) session.getAttribute("userId");
        stepUpdateReqDto.setApproverId(approverId);

        Workflow workflow = workflowService.approveStep(stepUpdateReqDto);

        ResponseDataDto<Workflow> response = new ResponseDataDto<>();
        response.setData(workflow);
        response.setMessage("Step approved successfully");
        response.setStatus("0");

        return ResponseEntity.ok(response);
	}

	@PostMapping("/applications/reject")
	public ResponseEntity<ResponseDataDto<Workflow>> rejectStep(@RequestBody StepUpdateReqDto stepUpdateReqDto) {
		 Long approverId = (Long) session.getAttribute("userId");
	        stepUpdateReqDto.setApproverId(approverId);

	        Workflow workflow = workflowService.rejectStep(stepUpdateReqDto);

	        ResponseDataDto<Workflow> response = new ResponseDataDto<>();
	        response.setData(workflow);
	        response.setMessage("Step rejected successfully");
	        response.setStatus("0");

	        return ResponseEntity.ok(response);
	}

	@PostMapping("/applications/workflows")
	public ResponseEntity<ResponseDataDto<List<WorkflowStepDTO>>> getApplicationWorkflow(
			@RequestBody ApplicationWorkflowReqDto applicationWorkflowReqDto) {

        List<WorkflowStepDTO> steps = workflowService.getWorkflowsByApplicant(applicationWorkflowReqDto);

        ResponseDataDto<List<WorkflowStepDTO>> response = new ResponseDataDto<>();
        response.setData(steps);
        response.setMessage(steps.isEmpty() ? "No workflow steps found" : "Workflow retrieved successfully");
        response.setStatus("0");

        return ResponseEntity.ok(response);
	}
}
