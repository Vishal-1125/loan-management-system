package com.twinline.loan_management_system.dto.response;

import lombok.Data;

@Data
public class ResponseDataDto<T> {
	private T data;
	private String message;
	private String status;
}
