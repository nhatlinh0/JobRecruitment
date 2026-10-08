package com.example.repository;

import com.example.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Job j SET j.domain = NULL WHERE j.domain.id = :domainId")
    void clearDomain(@Param("domainId") Integer id);

    Optional<Job> findBySlug(String slug);

    List<Job> findByDomainSlug(String domainSlug);
    List<Job> findBySkillsSlug(String skillSlug);
    List<Job> findByCompanySlug(String companySlug);

}
