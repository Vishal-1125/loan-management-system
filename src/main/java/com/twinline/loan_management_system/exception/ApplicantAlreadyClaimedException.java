package com.twinline.loan_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ApplicantAlreadyClaimedException extends RuntimeException {
    public ApplicantAlreadyClaimedException(String message) {
        super(message);
    }
}
