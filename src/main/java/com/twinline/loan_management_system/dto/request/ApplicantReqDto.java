package com.twinline.loan_management_system.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ApplicantReqDto {

	private Long roId;
	private String name;
	private BigDecimal loanAmount;
	private Integer tenureMonths;
	private BigDecimal income;
	private String contactDetails;
}

