package com.twinline.loan_management_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twinline.loan_management_system.dto.request.ApplicantReqDto;
import com.twinline.loan_management_system.dto.response.ApplicantResDto;
import com.twinline.loan_management_system.entity.Applicant;
import com.twinline.loan_management_system.service.ApplicantService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/ro")
public class ROController {

    private final ApplicantService applicantService;
    private final HttpSession session;

    public ROController(ApplicantService applicantService, HttpSession session) {
        this.applicantService = applicantService;
        this.session = session;
    }
    
    
    @PostMapping("/punchApplication")
    public Applicant punchApplication(@RequestBody ApplicantReqDto applicantReqDto) {
        Long roId = (Long) session.getAttribute("userId");
        applicantReqDto.setRoId(roId);
        return applicantService.createApplicant(applicantReqDto);
    }

    @GetMapping("/viewApplications")
    public List<ApplicantResDto> viewApplications() {
        Long roId = (Long) session.getAttribute("userId");
        return applicantService.getApplicantsByRO(roId);
    }
}
