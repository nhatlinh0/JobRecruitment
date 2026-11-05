package com.example.repository;

import com.example.entity.Job;
import com.example.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer>, JpaSpecificationExecutor<Job> {
    @Modifying
    @Transactional
    @Query("UPDATE Job j SET j.domain = NULL WHERE j.domain.id = :domainId")
    void clearDomain(@Param("domainId") Integer id);

    Optional<Job> findBySlug(String slug);
    Page<Job> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Job> findByTitleContainingIgnoreCaseAndStatus(String title, Status status, Pageable pageable);
    Page<Job> findByDomainSlugAndStatus(String domainSlug, Status status, Pageable pageable);
    Page<Job> findBySkillsSlugAndStatus(String skillSlug, Status status, Pageable pageable);
    Page<Job> findByCompanySlugAndStatus(String companySlug, Status status, Pageable pageable);
    List<Job> findAllByCompanyId(Integer companyId);

//    @Query("SELECT a.job FROM Application a WHERE a.user.id = :userId")
//    Page<Job> findJobsApplicationByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query("SELECT j FROM User u JOIN u.jobs j WHERE u.id = :userId")
    Page<Job> findSavedJobsByUserId(@Param("userId") Integer userId, Pageable pageable);

    Page<Job> findByStatus(Status status, Pageable pageable);
}
