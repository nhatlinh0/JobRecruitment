package com.example.repository;

import com.example.entity.Domain;
import com.example.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DomainRepository extends JpaRepository<Domain, Integer> {
    @Query(
            value = "select s from Domain d join d.skills s where d.id = :domainId",
            countQuery = "select count(s) from Domain d join d.skills s where d.id = :domainId"
    )
    Page<Skill> findSkillsByDomainId(@Param("domainId") Integer domainId, Pageable pageable);
}
