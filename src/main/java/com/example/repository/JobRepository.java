package com.example.repository;

import com.example.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JobRepository extends JpaRepository<Job, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Job SET Job.domain = NULL WHERE Job.domain.id = :domainId")
    void clearDomain(@Param("domainId") Integer id);
}
