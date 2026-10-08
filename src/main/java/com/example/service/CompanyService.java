package com.example.service;

import com.example.dto.CompanyRequest;
import com.example.dto.CompanyResponse;
import com.example.dto.JobResponse;
import com.example.entity.Company;
import com.example.entity.Industry;
import com.example.entity.User;
import com.example.repository.CompanyRepository;
import com.example.repository.IndustryRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CompanyResponse> getAllCompany() {
        return companyRepository.findAll().stream().map((x) -> new CompanyResponse(x)).toList();
    }

    public Company findByCompanyId(Integer id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Company"));
    }

    public Company findByCompanySlug(String slug) {
        return companyRepository.findBySlug(slug).orElseThrow(() -> new RuntimeException("Không tìm thấy Company"));
    }

    public List<CompanyResponse> findCompanyByIndustrySlug(String slug) {
        List<Company> list = companyRepository.findByIndustrySlug(slug);
        if (list.isEmpty()) {
            throw new RuntimeException("Company not found with slug: " + slug);
        }
        return list.stream().map(CompanyResponse::new).toList();
    }


    @Transactional
    public void saveCompany(CompanyRequest companyRequest) {
        //        BASIC
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Không tìm thấy User"));
        if (user.getCompany() != null) {
            throw new RuntimeException("User đã tồn tại Company");
        }

        Industry industry = industryRepository.findById(companyRequest.getIndustryId()).orElseThrow(() -> new RuntimeException("Không tìm thấy Industry"));

        Company company = new Company();
        company.setName(companyRequest.getName());
        company.setSlug(companyRequest.getSlug());
        company.setAddress(companyRequest.getAddress());
        company.setCompanySize(companyRequest.getCompanySize());
        company.setPhone(companyRequest.getPhone());
        company.setLogoUrl(companyRequest.getLogoUrl());
        company.setProfilePdfUrl(companyRequest.getProfilePDFUrl());
        company.setCountry(companyRequest.getCountry());
        company.setIndustry(industry);
        companyRepository.save(company);


         user.setCompany(company);
         userRepository.save(user);
    }

    public void updateCompany(CompanyRequest companyRequest, Integer id) {
        Industry industry = industryRepository.findById(companyRequest.getIndustryId()).orElseThrow(() -> new RuntimeException("Không tìm thấy Industry"));
        Company company = companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Industry"));

        company.setName(companyRequest.getName());
        company.setSlug(companyRequest.getSlug());
        company.setAddress(companyRequest.getAddress());
        company.setCompanySize(companyRequest.getCompanySize());
        company.setPhone(companyRequest.getPhone());
        company.setLogoUrl(companyRequest.getLogoUrl());
        company.setProfilePdfUrl(companyRequest.getProfilePDFUrl());
        company.setCountry(companyRequest.getCountry());
        company.setIndustry(industry);
        companyRepository.save(company);
    }

    @Transactional
    public void deleteCompany(Integer id) {
        userRepository.clearCompany(id);
        companyRepository.deleteById(id);
    }
}
