package com.twinline.loan_management_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.twinline.loan_management_system.dto.request.ApplicantReqDto;
import com.twinline.loan_management_system.dto.response.ApplicantResDto;
import com.twinline.loan_management_system.dto.response.ResponseDataDto;
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
    public ResponseEntity<ResponseDataDto<ApplicantResDto>> punchApplication(@RequestBody ApplicantReqDto applicantReqDto) {
        Long roId = (Long) session.getAttribute("userId");
        applicantReqDto.setRoId(roId);
        ApplicantResDto applicantResDto= applicantService.createApplicant(applicantReqDto);
        ResponseDataDto<ApplicantResDto> response = new ResponseDataDto<>();
        response.setData(applicantResDto);
        response.setMessage("Apllicant Created Successfully");
        response.setStatus("0");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/viewApplications")
    public ResponseEntity<ResponseDataDto<List<ApplicantResDto>>> viewApplications() {
        Long roId = (Long) session.getAttribute("userId");
        List<ApplicantResDto> applicantResDto= applicantService.getApplicantsByRO(roId);
        ResponseDataDto<List<ApplicantResDto>> response = new ResponseDataDto<>();
        response.setData(applicantResDto);
        response.setMessage("Data Fetch Successfully");
        response.setStatus("0");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
