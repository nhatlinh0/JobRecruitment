package com.example.repository;

import com.example.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobReposotory extends JpaRepository<Job, Long> {
}
