package com.example.controller;

import com.example.dto.DomainRequest;
import com.example.dto.DomainResponse;
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
