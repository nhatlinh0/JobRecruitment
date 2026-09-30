package com.example.repository;

import com.example.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE UserInfo SET UserInfo.domain = NULL WHERE UserInfo.domain.id = :domainId")
    void clearDomain(@Param("domainId") Integer id);
}
