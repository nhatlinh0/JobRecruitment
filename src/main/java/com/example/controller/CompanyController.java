package com.example.controller;

import com.example.dto.CompanyRequest;
import com.example.dto.CompanyResponse;
import com.example.dto.JobResponse;
import com.example.entity.Company;
import com.example.service.CompanyService;
import com.example.service.JobService;
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

    @Autowired
    private JobService jobService;

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
    @GetMapping("/id/{id}")
    public ResponseEntity<CompanyResponse> findCompanyId(@PathVariable Integer id) {
        Company company =  companyService.findByCompanyId(id);
        return ResponseEntity.ok(new CompanyResponse(company));
    }
//--------------
    @GetMapping("/domain/{domainSlug}")
    public ResponseEntity<?> findCompaniesByIndustrySlug(@PathVariable String domainSlug) {
        List<CompanyResponse> list =  companyService.findCompanyByIndustrySlug(domainSlug);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        Company company =  companyService.findByCompanySlug(slug);
        return ResponseEntity.ok(new CompanyResponse(company));
    }

    @GetMapping("/{slug}/jobs")
    public ResponseEntity<?> findJobsByCompany(@PathVariable String slug) {
        List<JobResponse> list =  jobService.findJobsByCompanySlug(slug);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//--------------
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
