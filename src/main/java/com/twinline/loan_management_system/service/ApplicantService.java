package com.twinline.loan_management_system.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import com.twinline.loan_management_system.dto.request.ApplicantReqDto;
import com.twinline.loan_management_system.dto.request.ApplicationReqDto;
import com.twinline.loan_management_system.dto.request.ClaimApplicationReqDto;
import com.twinline.loan_management_system.dto.response.ApplicantResDto;
import com.twinline.loan_management_system.entity.Applicant;

public interface ApplicantService {
    Applicant createApplicant(ApplicantReqDto applicantReqDto);
    List<ApplicantResDto> getApplicantsByRO(Long roId);
    List<Applicant> getApplications(@RequestBody ApplicationReqDto applicationReqDtos);
    public Applicant claimApplicant(ClaimApplicationReqDto claimApplicationReqDto);
}

