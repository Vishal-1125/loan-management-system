package com.twinline.loan_management_system.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.twinline.loan_management_system.dto.request.ApplicantReqDto;
import com.twinline.loan_management_system.dto.request.ApplicationReqDto;
import com.twinline.loan_management_system.dto.request.ClaimApplicationReqDto;
import com.twinline.loan_management_system.dto.response.ApplicantResDto;
import com.twinline.loan_management_system.entity.Applicant;
import com.twinline.loan_management_system.entity.User;
import com.twinline.loan_management_system.entity.Workflow;
import com.twinline.loan_management_system.entity.WorkflowMaster;
import com.twinline.loan_management_system.repo.ApplicantRepository;
import com.twinline.loan_management_system.repo.UserRepository;
import com.twinline.loan_management_system.repo.WorkflowMasterRepository;
import com.twinline.loan_management_system.repo.WorkflowRepository;
import com.twinline.loan_management_system.service.ApplicantService;

@Service
public class ApplicantServiceImpl implements ApplicantService {

	private final ApplicantRepository applicantRepository;
	private final WorkflowRepository workflowRepository;
	private final WorkflowMasterRepository workflowMasterRepository;
	private final UserRepository userRepository;

	public ApplicantServiceImpl(ApplicantRepository applicantRepository, WorkflowRepository workflowRepository,
			WorkflowMasterRepository workflowMasterRepository, UserRepository userRepository) {
		super();
		this.applicantRepository = applicantRepository;
		this.workflowRepository = workflowRepository;
		this.workflowMasterRepository = workflowMasterRepository;
		this.userRepository = userRepository;
	}

	@Override
	public Applicant createApplicant(ApplicantReqDto applicantReqDto) {
		Applicant applicant = new Applicant();
		BeanUtils.copyProperties(applicantReqDto, applicant);

		User ro = userRepository.findById(applicantReqDto.getRoId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		applicant.setStatus("Pending");
		applicant.setRo(ro);
		applicant.setCreatedAt(LocalDateTime.now());
		applicant.setUpdatedAt(LocalDateTime.now());
		Applicant saved = applicantRepository.save(applicant);

//		List<WorkflowMaster> steps = workflowMasterRepository.findAllByOrderByStepOrderAsc();
//		List<Workflow> workflows = steps.stream()
//				.map(step -> Workflow.builder().applicant(saved).workflowStep(step)
//						.status(step.getStepOrder() == 1 ? "Pending" : "Not Started").createdAt(LocalDateTime.now())
//						.build())
//				.collect(Collectors.toList());
//
//		workflowRepository.saveAll(workflows);
		
	    WorkflowMaster step = workflowMasterRepository.findByStepOrder(1);
	    Workflow workflow= Workflow.builder().applicant(saved).workflowStep(step).status("Pending").createdAt(LocalDateTime.now()).build();
	    workflowRepository.save(workflow);
		return saved;

	}

	@Override
	public List<ApplicantResDto> getApplicantsByRO(Long roId) {

		List<Applicant> applicants = applicantRepository.findByRoUserId(roId);

		return applicants.stream().map(applicant -> {
			ApplicantResDto dto = new ApplicantResDto();
			dto.setApplicantId(applicant.getApplicantId());
			dto.setName(applicant.getName());
			dto.setLoanAmount(applicant.getLoanAmount());
			dto.setTenureMonths(applicant.getTenureMonths());
			dto.setIncome(applicant.getIncome());
			dto.setContactDetails(applicant.getContactDetails());
			dto.setStatus(applicant.getStatus());
			return dto;
		}).toList();

	}

//    @Override
//    public Applicant getApplicant(Long id) {
//        return applicantRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Applicant not found"));
//    }

	@Override
	public Applicant claimApplicant(ClaimApplicationReqDto claimApplicationReqDto) {
		Applicant applicant = applicantRepository.findById(claimApplicationReqDto.getApplicantId())
				.orElseThrow(() -> new RuntimeException("Applicant not found"));

		if (applicant.getClaimedBy() != null) {
			throw new RuntimeException("Applicant already claimed by another approver");
		}

		User approver = userRepository.findById(claimApplicationReqDto.getApproverId())
				.orElseThrow(() -> new RuntimeException("Approver not found"));

		applicant.setClaimedBy(approver);
		applicant.setUpdatedAt(LocalDateTime.now());
		return applicantRepository.save(applicant);
	}

	@Override
	public List<Applicant> getApplications(ApplicationReqDto dto) {
		switch (dto.getApplicationBucket()) {
		case "Unclaimed":
			return applicantRepository.getApplications( Arrays.asList("Pending"), null);

		case "Claimed":
			return applicantRepository.getApplications(Arrays.asList("Pending", "In Progress"),
					dto.getApproverId());

		case "Approved":
			return applicantRepository.getApplications( Arrays.asList("Approved"),
					dto.getApproverId());

		case "Rejected":
			return applicantRepository.getApplications( Arrays.asList("Rejected"),
					dto.getApproverId());

		default:
			throw new IllegalArgumentException("Invalid application bucket: " + dto.getApplicationBucket());

		}
	}

}
