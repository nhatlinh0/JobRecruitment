package com.example.repository;

import com.example.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleReposotory extends JpaRepository<Role, Long> {
}
