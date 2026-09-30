package com.example.repository;

import com.example.entity.Industry;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IndustryRepository extends JpaRepository<Industry, Integer> {
}
