package com.example.dto;

import com.example.entity.Company;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyResponse {
    private  Integer id;
    private String name;
    private String slug;
    private String address;
    private String companySize;
    private String phone;
    private String logoUrl;
    private String profilePDFUrl;
    private String status;
    private String country;
    private String industryName;
    private String createAt;

    public CompanyResponse(Company company) {
        this.id = company.getId();
        this.name = company.getName();
        this.slug = company.getSlug();
        this.address = company.getAddress();
        this.companySize = company.getCompanySize();
        this.phone = company.getPhone();
        this.logoUrl = company.getLogoUrl();
        this.profilePDFUrl = company.getProfilePdfUrl();
        this.status = company.getStatus().toString();
        this.country = company.getCountry();
        this.industryName = company.getIndustry().getName();
        this.createAt = company.getCreatedAt().toString();
    }
}
