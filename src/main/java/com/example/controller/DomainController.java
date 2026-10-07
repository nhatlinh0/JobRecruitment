package com.example.controller;

import com.example.dto.DomainRequest;
import com.example.dto.DomainResponse;
import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
import com.example.entity.Domain;
import com.example.service.DomainService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/domains")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @GetMapping
    public ResponseEntity<List<DomainResponse>> getAllDomain() {
        return ResponseEntity.ok(domainService.findAll());
    }

    @PostMapping()
    public ResponseEntity<?> updateDomain(@Valid @RequestBody DomainRequest domainRequest) {
        domainService.saveDomain(domainRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{domainId}")
    public ResponseEntity<List<SkillResponse>> getSkillsDomain (@PathVariable Integer domainId) {
        List<SkillResponse> skills = domainService.getSkillsFromDomain(domainId);
        return ResponseEntity.ok(skills);
    }

    @PostMapping("/{domainId}/skill")
    public ResponseEntity<?> createSkillFromDomain(@Valid @RequestBody SkillRequest skillRequest, @PathVariable Integer domainId){
        domainService.saveSkillFromDomain(skillRequest, domainId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("{domainId}/skill/{skillId}")
    public ResponseEntity<?> deleteSkillFromDomain(@PathVariable Integer skillId, @PathVariable Integer domainId){
        domainService.deleteSkillFromDomain(skillId, domainId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> createDomain(@Valid @RequestBody DomainRequest domainRequest, @PathVariable Integer id) {
        domainService.updateDomain(domainRequest, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDomain(@PathVariable int id) {
        try {
            domainService.deleteDomain(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
