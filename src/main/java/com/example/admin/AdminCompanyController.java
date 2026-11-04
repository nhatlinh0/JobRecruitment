package com.example.admin;

import com.example.dto.CompanyResponse;
import com.example.dto.JobResponse;
import com.example.dto.PageResponse;
import com.example.entity.Company;
import com.example.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/companies")
public class AdminCompanyController {
    @Autowired
    private CompanyService companyService;

    @PutMapping("/{companyId}/accept")
    public ResponseEntity<CompanyResponse> acceptCompany(@PathVariable Integer companyId) {
        Company company = companyService.acceptCompany(companyId);
        return ResponseEntity.ok(new CompanyResponse(company));
    }

    @PutMapping("/{companyId}/reject")
    public ResponseEntity<CompanyResponse> rejectCompany(@PathVariable Integer companyId) {
        Company company = companyService.rejectCompany(companyId);
        return ResponseEntity.ok(new CompanyResponse(company));
    }

    @PutMapping("/{companyId}/suspend")
    public ResponseEntity<CompanyResponse> suspendCompany(@PathVariable Integer companyId) {
        Company company = companyService.suspendCompany(companyId);
        return ResponseEntity.ok(new CompanyResponse(company));
    }

    @GetMapping("/status")
    public ResponseEntity<PageResponse<CompanyResponse>> getCompanyByStatus(
            @RequestParam(required = false) String status,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        PageResponse<CompanyResponse> companies = companyService.findCompanyStatus(status, pageable);
        return ResponseEntity.ok(companies);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<PageResponse<CompanyResponse>> findByName(@PathVariable String name, @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
    Pageable pageable) {
        PageResponse<CompanyResponse> companies =  companyService.findByCompanyName(name, pageable);
        return ResponseEntity.ok(companies);
    }
}
