package com.example.service;

import com.example.entity.Domain;
import com.example.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainService {
    @Autowired
    private DomainRepository domainRepository;

    public Domain saveDomain(Domain domain) {
        return domainRepository.save(domain);
    }

    public List<Domain> findAll() {
        return domainRepository.findAll();
    }

    public void deleteDomain(int id) {
        domainRepository.deleteById(id);
    }
}
