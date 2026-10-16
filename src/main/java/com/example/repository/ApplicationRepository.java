package com.example.repository;

import com.example.entity.Application;
import com.example.entity.Job;
import com.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    Page<Application> findAllByUser(User user, Pageable pageable);

    @Query(
            value = "select a from Application a join a.job j where j.id = :jobId ",
            countQuery = "select count(a) from Application a join a.job j where j.id = :jobId "
    )
    Page<Application> findAllByJob(@Param("jobId") Integer jobId, Pageable pageable);
}
