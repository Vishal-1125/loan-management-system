package com.twinline.loan_management_system.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private HttpSession session;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		session.setAttribute("userId", userDetails.getUserId());
		session.setAttribute("role", userDetails.getRole().getRoleName());
		if (userDetails.getRole().getRoleName().equals("RO")) {
			response.sendRedirect("/ro/dashboard");
		} else if (userDetails.getRole().getRoleName().equals("APPROVER")) {
			response.sendRedirect("/approver/dashboard");
		} else {
			response.sendRedirect("/login");
		}
	}
}
