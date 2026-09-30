package com.example.service;

import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
import com.example.entity.Domain;
import com.example.entity.Skill;
import com.example.repository.DomainRepository;
import com.example.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private DomainRepository domainRepository;

    public void saveSkill(SkillRequest skillRequest) {
        Domain domain = domainRepository.findById(skillRequest.getDomainId()).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        Skill skill = new Skill();
        skill.setName(skillRequest.getName());
        skill.setDomain(domain);
        skillRepository.save(skill);
    }

    public void updateSkill(SkillRequest skillRequest, Integer id) {
        Skill skill = skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy skill"));
        Domain domain = domainRepository.findById(skillRequest.getDomainId()).orElseThrow(() -> new RuntimeException("Không tìm thấy domain"));
        skill.setName(skillRequest.getName());
        skill.setDomain(domain);
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
