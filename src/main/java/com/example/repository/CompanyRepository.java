package com.example.repository;

import com.example.entity.Company;
import com.example.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Company c SET c.industry = NULL WHERE c.industry.id = :industryId")
    void clearIndustry(@Param("industryId") Integer id);

    Optional<Company> findBySlug(String slug);

    Page<Company> findByIndustrySlug(String slug, Pageable pageable);
    boolean existsByUser(User user);
}
