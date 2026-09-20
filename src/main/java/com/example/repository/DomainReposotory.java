package com.example.repository;

import com.example.entity.Domain;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainReposotory extends JpaRepository<Domain, Long> {
}
