package com.example.service;

import com.example.dto.DomainRequest;
import com.example.dto.DomainResponse;
import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
import com.example.entity.Domain;
import com.example.entity.Skill;
import com.example.repository.DomainRepository;
import com.example.repository.JobRepository;
import com.example.repository.SkillRepository;
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
    private SkillRepository skillRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    public void saveDomain(DomainRequest domainRequest) {
        Domain domain = new Domain();
        domain.setName(domainRequest.getName());
        domain.setSlug(domainRequest.getSlug());
        domainRepository.save(domain);
    }

    public void updateDomain (DomainRequest domainRequest, Integer id) {
        Domain domain = domainRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        domain.setName(domainRequest.getName());
        domain.setSlug(domainRequest.getSlug());
        domainRepository.save(domain);
    }

    public List<DomainResponse> findAll() {
        return domainRepository.findAll().stream().map((x) -> new DomainResponse(x)).toList();
    }

    public List<SkillResponse> getSkillsFromDomain(Integer domainId) {
        Domain domain = domainRepository.findById(domainId).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        return domain.getSkills().stream().map((x) -> new SkillResponse(x)).toList();
    }

    @Transactional
    public void saveSkillFromDomain(SkillRequest skillRequest, Integer domainId) {
        Domain domain = domainRepository.findById(domainId).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        Skill skill = new Skill();
        skill.setName(skillRequest.getName());
        skill.setSlug(skillRequest.getSlug());

        skillRepository.save(skill);
        domain.getSkills().add(skill);
        domainRepository.save(domain);
    }

    @Transactional
    public void deleteSkillFromDomain(Integer skillId, Integer domainId) {
        Domain domain = domainRepository.findById(domainId).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new RuntimeException("Không tìm thấy skill"));

        domain.getSkills().remove(skill);
        domainRepository.save(domain);
    }

    @Transactional
    public void deleteDomain(Integer id) {
        jobRepository.clearDomain(id);
        userInfoRepository.clearDomain(id);
        domainRepository.deleteById(id);
    }
}
