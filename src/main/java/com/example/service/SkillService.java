package com.example.service;

import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
import com.example.entity.Domain;
import com.example.entity.Skill;
import com.example.repository.DomainRepository;
import com.example.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private DomainRepository domainRepository;

    public List<SkillResponse> getSkillsFromDomain(Integer domainId) {
        Domain domain = domainRepository.findById(domainId).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        return domain.getSkills().stream().map((x) -> new SkillResponse(x)).toList();
    }

    @Transactional()
    public void saveSkill(SkillRequest skillRequest, Integer domainId) {
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

    public void updateSkill(SkillRequest skillRequest, Integer id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy skill"));
        skill.setName(skillRequest.getName());
        skill.setSlug(skillRequest.getSlug());
        skillRepository.save(skill);
    }

    public List<SkillResponse> getAllSkill() {
        return skillRepository.findAll().stream().map((x) -> new SkillResponse(x)).toList();
    }

    public void deleteSkill(Integer id) {
        skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy industry"));
        skillRepository.deleteById(id);
    }
}
