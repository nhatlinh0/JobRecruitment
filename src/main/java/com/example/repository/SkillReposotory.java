package com.example.repository;

import com.example.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillReposotory extends JpaRepository<Skill, Long> {
}
