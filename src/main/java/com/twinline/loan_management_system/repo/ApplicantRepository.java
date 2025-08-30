package com.twinline.loan_management_system.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twinline.loan_management_system.entity.Applicant;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {

    List<Applicant> findByRoUserId(Long roId);
    
    @Query("SELECT a FROM Applicant a " +
    	       "WHERE a.status IN :statuses " +
    	       "AND ((:claimedById IS NULL AND a.claimedBy.userId IS NULL) " +
    	       "     OR a.claimedBy.userId = :claimedById)")
    	List<Applicant> getApplications(@Param("statuses") List<String> statuses,
    	                                @Param("claimedById") Long claimedById);


    List<Applicant> findByRoUserIdInAndStatusIn(List<Long> roIdList, List<String> statusList);
}