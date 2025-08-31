package com.twinline.loan_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.twinline.loan_management_system.dto.response.ResponseDataDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenActionException.class)
    public ResponseEntity<ResponseDataDto<Object>> handleForbidden(ForbiddenActionException ex) {
        ResponseDataDto<Object> response = new ResponseDataDto<>();
        response.setData(null);
        response.setMessage(ex.getMessage());
        response.setStatus("2");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDataDto<Object>> handleGenericException(Exception ex) {
        ResponseDataDto<Object> response = new ResponseDataDto<>();
        response.setData(null);
        response.setMessage("Something went wrong: " + ex.getMessage());
        response.setStatus("2");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
