package com.example.admin;

import com.example.dto.CompanyResponse;
import com.example.entity.Company;
import com.example.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
