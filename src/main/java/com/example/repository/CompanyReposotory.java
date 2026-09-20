package com.example.repository;

import com.example.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyReposotory extends JpaRepository<Company, Long> {
}
