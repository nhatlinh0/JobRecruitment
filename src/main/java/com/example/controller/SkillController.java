package com.example.controller;

import com.example.dto.PageResponse;
import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
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
@RequestMapping("/api/v1/skills")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<PageResponse<SkillResponse>> getAllSkill(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(skillService.getAllSkill(pageable));
    }

//    @PostMapping("/domain/{domainId}")
//    public ResponseEntity<?> createSkill(@Valid @RequestBody SkillRequest skillRequest, @PathVariable Integer domainId){
//        skillService.saveSkill(skillRequest, domainId);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }
//
//    @DeleteMapping("/domain/{domainId}/skill/{skillId}")
//    public ResponseEntity<?> deleteSkillFromDomain(@PathVariable Integer skillId, @PathVariable Integer domainId){
//        skillService.deleteSkillFromDomain(skillId, domainId);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(@Valid @RequestBody SkillRequest skillRequest, @PathVariable Integer id){
        skillService.updateSkill(skillRequest ,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkill(@PathVariable Integer id) {
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
