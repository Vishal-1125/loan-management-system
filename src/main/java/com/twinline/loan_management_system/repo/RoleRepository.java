package com.twinline.loan_management_system.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twinline.loan_management_system.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}

