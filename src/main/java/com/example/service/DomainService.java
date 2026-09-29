package com.example.service;

import com.example.dto.DomainResponse;
import com.example.entity.Domain;
import com.example.repository.DomainRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainService {
    @Autowired
    private DomainRepository domainRepository;

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

    public void deleteDomain(int id) {
        domainRepository.deleteById(id);
    }
}
