package com.twinline.loan_management_system.dto.request;

import lombok.Data;

@Data
public class StepUpdateReqDto {
	private Long applicantId;
	private Long approverId;
	private String comments;
}
