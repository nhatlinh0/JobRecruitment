package com.example.controller;

import com.example.dto.CompanyRequest;
import com.example.dto.CompanyResponse;
import com.example.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @GetMapping()
    public ResponseEntity<List<CompanyResponse>> getAllCompany() {
        List<CompanyResponse> list =  companyService.getAllCompany();
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    //ADMIN
//    @GetMapping("/{id}")
//    public ResponseEntity<?> findCompanyId() {
//
//    }
//
//    @GetMapping("/{domainSlug}")
//    public ResponseEntity<?> findCompaniesByDomainSlug() {
//
//    }

//    @GetMapping("/{slug}")
//    public ResponseEntity<?> findCompaniesBySlug() {
//
//    }
//
//    @GetMapping("/{id}/jobs")
//    public ResponseEntity<?> findJobsByCompany() {
//
//    }

    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequest companyRequest) {
        companyService.saveCompany(companyRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@Valid @RequestBody CompanyRequest companyRequest, @PathVariable Integer id) {
        companyService.updateCompany(companyRequest, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
