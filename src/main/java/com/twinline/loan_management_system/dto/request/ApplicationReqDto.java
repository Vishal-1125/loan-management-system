package com.twinline.loan_management_system.dto.request;

import lombok.Data;

@Data
public class ApplicationReqDto {

	private String applicationBucket;
	private Long approverId;
	private Long applicantId;
}
