package com.twinline.loan_management_system.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantResDto {

	private Long applicantId;

	private String name;

	private BigDecimal loanAmount;

	private Integer tenureMonths;

	private BigDecimal income;

	private String contactDetails;

	private String status;

}
