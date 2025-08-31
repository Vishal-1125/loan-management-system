package com.twinline.loan_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title="Twinline LMS" ,version="1.0" , description = "API Documentation"))
public class LoanManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanManagementSystemApplication.class, args);
	}

}
