package com.example.repository;

import com.example.entity.Application;
import com.example.entity.Job;
import com.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findAllByUser(User user);
    List<Application> findAllByJob(Job job);
}
