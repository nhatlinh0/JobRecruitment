package com.example.repository;

import com.example.entity.Company;
import com.example.entity.Job;
import com.example.entity.User;
import com.example.enums.Status;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Company c WHERE c.id = :id")
    Optional<Company> findByIdForUpdate(@Param("id") Integer id);

    @Modifying
    @Query("UPDATE Company c SET c.credits = c.credits + :credits " +
            "WHERE c.id = :id AND c.credits <= :maxBalance")
    int addCredits(@Param("id") Integer id, @Param("credits") int credits,
                   @Param("maxBalance") int maxBalance);

    @Modifying
    @Transactional
    @Query("UPDATE Company c SET c.industry = NULL WHERE c.industry.id = :industryId")
    void clearIndustry(@Param("industryId") Integer id);

    Optional<Company> findBySlug(String slug);
    Page<Company> findByNameContainingIgnoreCase(String name, Pageable pageable);


    Page<Company> findByIndustrySlug(String slug, Pageable pageable);
    boolean existsByUser(User user);
    Page<Company> findByStatus(Status status, Pageable pageable);

}
