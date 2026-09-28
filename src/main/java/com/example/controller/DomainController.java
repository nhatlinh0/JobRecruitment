package com.example.controller;

import com.example.entity.Domain;
import com.example.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/domain")
public class DomainController {
    @Autowired
    private DomainService domainService;

    @GetMapping
    public ResponseEntity<List<Domain>> getAllDomain() {
        return ResponseEntity.ok(domainService.findAll());
    }

    @PostMapping()
    public ResponseEntity<?> createDomain(@RequestBody Domain domain) {
        try {
            domainService.saveDomain(domain);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
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
