package com.example.controller;

import com.example.dto.SkillRequest;
import com.example.dto.SkillResponse;
import com.example.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/skill")
public class SkillController {
    @Autowired
    private SkillService skillService;

    @GetMapping
    public ResponseEntity<List<SkillResponse>> getAllSkill() {
        List<SkillResponse> list = skillService.getAllSkill();
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createSkill(@Valid @RequestBody SkillRequest skillRequest){
        skillService.saveSkill(skillRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSkill(@Valid @RequestBody SkillRequest skillRequest, @PathVariable Integer id){
        skillService.updateSkill(skillRequest ,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIndustry(@PathVariable Integer id) {
        skillService.deleteSkill(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
