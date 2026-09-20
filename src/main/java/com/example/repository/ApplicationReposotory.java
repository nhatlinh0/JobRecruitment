package com.example.repository;

import com.example.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationReposotory extends JpaRepository<Application, Long> {
}
