package com.example.repository;

import com.example.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Company SET Company.industry = NULL WHERE Company.industry.id = :industryId")
    void clearIndustry(@Param("industryId") Integer id);
}
