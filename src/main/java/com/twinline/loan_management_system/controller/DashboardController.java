package com.twinline.loan_management_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {


    @GetMapping("/approver/dashboard")
    public String showApproverDashboard() {
        return "approver";
    }
    
    @GetMapping("/ro/dashboard")
    public String showRODashboard() {
        return "ro";
    }

    
}
