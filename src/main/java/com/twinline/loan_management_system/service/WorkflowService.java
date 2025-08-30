package com.twinline.loan_management_system.service;

import java.util.List;

import com.twinline.loan_management_system.dto.request.ApplicationWorkflowReqDto;
import com.twinline.loan_management_system.dto.request.StepUpdateReqDto;
import com.twinline.loan_management_system.dto.response.WorkflowStepDTO;
import com.twinline.loan_management_system.entity.Workflow;

public interface WorkflowService {
    Workflow approveStep(StepUpdateReqDto stepUpdateReqDto);
    Workflow rejectStep(StepUpdateReqDto stepUpdateReqDto);
    List<WorkflowStepDTO> getWorkflowsByApplicant(ApplicationWorkflowReqDto applicationWorkflowReqDto);
}
