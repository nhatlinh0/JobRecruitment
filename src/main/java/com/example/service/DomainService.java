package com.example.service;

import com.example.dto.DomainResponse;
import com.example.entity.Domain;
import com.example.repository.DomainRepository;
import com.example.repository.JobRepository;
import com.example.repository.UserInfoRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DomainService {
    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public void saveDomain(Domain domain) {
         domainRepository.save(domain);
    }

    public void updateDomain (Domain domain, Integer id) {
        Domain newDomain = domainRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        newDomain.setName(domain.getName());
         domainRepository.save(newDomain);
    }

    public List<DomainResponse> findAll() {
        return domainRepository.findAll().stream().map((x) -> new DomainResponse(x)).toList();
    }

    @Transactional
    public void deleteDomain(Integer id) {
        domainRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        jobRepository.clearDomain(id);
        userInfoRepository.clearDomain(id);
        domainRepository.deleteById(id);
    }
}
