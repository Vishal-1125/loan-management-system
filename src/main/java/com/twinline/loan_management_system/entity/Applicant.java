package com.twinline.loan_management_system.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "applicant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Applicant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicantId;

    private String name;

    private BigDecimal loanAmount;

    private Integer tenureMonths;

    private BigDecimal income;

    private String contactDetails;

    @ManyToOne
    @JoinColumn(name = "ro_id", nullable = false)
    private User ro;
    
    @ManyToOne
    @JoinColumn(name = "claimed_by")
    private User claimedBy;


    @Column(nullable = false)
    private String status = "Pending";

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
