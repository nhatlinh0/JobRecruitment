package com.example.controller;

import com.example.dto.CompanyRequest;
import com.example.dto.CompanyResponse;
import com.example.dto.JobResponse;
import com.example.dto.PageResponse;
import com.example.entity.Company;
import com.example.security.UserDetailsCustom;
import com.example.service.CompanyService;
import com.example.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<PageResponse<CompanyResponse>> getAllCompany(
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        return ResponseEntity.ok(companyService.getAllCompany(pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/id/{id}")
    public ResponseEntity<CompanyResponse> findCompanyId(@PathVariable Integer id) {
        Company company =  companyService.findByCompanyId(id);
        return ResponseEntity.ok(new CompanyResponse(company));
    }

    @GetMapping("/industry/{industrySlug}")
    public ResponseEntity<PageResponse<CompanyResponse>> findCompaniesByIndustrySlug(@PathVariable String industrySlug, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
    Pageable pageable) {
        return ResponseEntity.ok(companyService.findCompanyByIndustrySlug(industrySlug, pageable));

    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<?> findBySlug(@PathVariable String slug) {
        Company company =  companyService.findByCompanySlug(slug);
        return ResponseEntity.ok(new CompanyResponse(company));
    }

    @GetMapping("/{slug}/jobs")
    public ResponseEntity<PageResponse<JobResponse>> findJobsByCompany(@PathVariable String slug,
                                                                       @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        PageResponse<JobResponse> list =  jobService.findJobsByCompanySlug(slug, pageable);
        return ResponseEntity.ok(list);
    }

//    @GetMapping("/jobs")
//    public ResponseEntity<?> findJobsByCompany(@AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
//        List<JobResponse> list =  jobService.findAllJobsByCompany(userDetailsCustom.getUserEntity());
//        if (!list.isEmpty()) {
//            return ResponseEntity.ok(list);
//        } else {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @PreAuthorize("hasRole('RECRUIT')")
    @PostMapping
    public ResponseEntity<?> createCompany(@Valid @RequestBody CompanyRequest companyRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        companyService.saveCompany(companyRequest, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('RECRUIT')")
    @PutMapping()
    public ResponseEntity<?> updateCompany(@Valid @RequestBody CompanyRequest companyRequest, @AuthenticationPrincipal UserDetailsCustom userDetailsCustom) {
        companyService.updateCompany(companyRequest, userDetailsCustom.getUserEntity());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Integer id) {
        companyService.deleteCompany(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
