package com.example.controller;

import com.example.dto.*;
import com.example.entity.Domain;
import com.example.service.DomainService;
import com.example.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/domains")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<PageResponse<DomainResponse>> getAllDomain(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(domainService.findAll(pageable));
    }

    @GetMapping("/{domainId}")
    public ResponseEntity<PageResponse<SkillResponse>> getSkillsDomain (@PathVariable Integer domainId, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<SkillResponse> skills = skillService.getSkillsFromDomain(domainId, pageable);
        return ResponseEntity.ok(skills);
    }

    @PostMapping("/{domainId}/skill")
    public ResponseEntity<?> createSkillFromDomain(@Valid @RequestBody SkillRequest skillRequest, @PathVariable Integer domainId){
        skillService.saveSkill(skillRequest, domainId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{domainId}/skill")
    public ResponseEntity<?> updateSkillFromDomain(@Valid @RequestBody SkillRequest skillRequest, @PathVariable Integer domainId){
        skillService.updateSkill(skillRequest, domainId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{domainId}/skill/{skillId}")
    public ResponseEntity<?> deleteSkillFromDomain(@PathVariable Integer skillId, @PathVariable Integer domainId){
        skillService.deleteSkillFromDomain(skillId, domainId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
