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
import com.twinline.loan_management_system.exception.ApplicantAlreadyClaimedException;
import com.twinline.loan_management_system.exception.ForbiddenActionException;
import com.twinline.loan_management_system.exception.ResourceNotFoundException;
import com.twinline.loan_management_system.repo.ApplicantRepository;
import com.twinline.loan_management_system.repo.UserRepository;
import com.twinline.loan_management_system.repo.WorkflowMasterRepository;
import com.twinline.loan_management_system.repo.WorkflowRepository;
import com.twinline.loan_management_system.service.ApplicantService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
	public ApplicantResDto createApplicant(ApplicantReqDto applicantReqDto) {
		Applicant applicant = new Applicant();
		ApplicantResDto applicantResDto = new ApplicantResDto();
		BeanUtils.copyProperties(applicantReqDto, applicant);

		User ro = userRepository.findById(applicantReqDto.getRoId())
				.orElseThrow(() -> new ForbiddenActionException("User not found"));

		applicant.setStatus("Pending");
		applicant.setRo(ro);
		applicant.setCreatedAt(LocalDateTime.now());
		applicant.setUpdatedAt(LocalDateTime.now());
		Applicant saved = applicantRepository.save(applicant);
		
	    WorkflowMaster step = workflowMasterRepository.findByStepOrder(1);
	    Workflow workflow= Workflow.builder().applicant(saved).workflowStep(step).status("Pending").createdAt(LocalDateTime.now()).build();
	    workflowRepository.save(workflow);
	    BeanUtils.copyProperties(saved, applicantResDto);
		return applicantResDto;

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
	public ApplicantResDto claimApplicant(ClaimApplicationReqDto claimApplicationReqDto) {
		
		ApplicantResDto applicantResDto  = new ApplicantResDto();
		
		Applicant applicant = applicantRepository.findById(claimApplicationReqDto.getApplicantId())
				.orElseThrow(() -> new ResourceNotFoundException("Applicant not found"));

		if (applicant.getClaimedBy() != null) {
			throw new ApplicantAlreadyClaimedException("Applicant already claimed by another approver");
		}

		User approver = userRepository.findById(claimApplicationReqDto.getApproverId())
				.orElseThrow(() -> new ForbiddenActionException("Approver not found"));

		applicant.setClaimedBy(approver);
		applicant.setUpdatedAt(LocalDateTime.now());
		
		
		BeanUtils.copyProperties(applicantRepository.save(applicant),applicantResDto);
		
		return applicantResDto;
	}

	@Override
	public List<Applicant> getApplications(ApplicationReqDto dto) {
		
		log.info(""+dto);
		if(dto.getApplicantId()!=null) {
			return Arrays.asList(applicantRepository.findById(dto.getApplicantId()).orElseThrow(()-> new ResourceNotFoundException("No Applicant Found with this Id "+dto.getApplicantId())));
		}
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
