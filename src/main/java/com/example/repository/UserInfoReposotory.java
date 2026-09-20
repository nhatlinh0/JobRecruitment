package com.example.repository;

import com.example.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoReposotory extends JpaRepository<UserInfo, Long> {
}
