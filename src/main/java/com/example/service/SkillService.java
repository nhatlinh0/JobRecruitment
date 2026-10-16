package com.example.service;

import com.example.dto.PageResponse;
import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
import com.example.entity.Domain;
import com.example.entity.Skill;
import com.example.repository.DomainRepository;
import com.example.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private DomainRepository domainRepository;

    public PageResponse<SkillResponse> getSkillsFromDomain(Integer domainId, Pageable pageable) {
        if (!domainRepository.existsById(domainId)) {
            throw new RuntimeException("Không tìm thấy domain");
        }

        return PageResponse.from(
                domainRepository.findSkillsByDomainId(domainId, pageable)
                        .map(SkillResponse::new)
        );
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

    public PageResponse<SkillResponse> getAllSkill(Pageable pageable) {
        return PageResponse.from(skillRepository.findAll(pageable).map(SkillResponse::new));
    }

    public void deleteSkill(Integer id) {
        skillRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy industry"));
        skillRepository.deleteById(id);
    }
}
