package com.example.service;

import com.example.dto.IndustryRequest;
import com.example.dto.IndustryResponse;
import com.example.entity.Domain;
import com.example.entity.Industry;
import com.example.repository.CompanyRepository;
import com.example.repository.IndustryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class IndustryService {
    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public void saveIndustry(IndustryRequest industryRequest) {
        Industry industry = new Industry();
        industry.setName(industryRequest.getName());
        industry.setSlug(industryRequest.getSlug());
        industry.setDescription(industryRequest.getDescription());
        industryRepository.save(industry);
    }

    public void updateIndustry(IndustryRequest industryRequest, Integer id) {
        Industry industry  = industryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy industry"));
        industry.setName(industryRequest.getName());
        industry.setSlug(industryRequest.getSlug());
        industry.setDescription(industryRequest.getDescription());
        industryRepository.save(industry);
    }

    public List<IndustryResponse> getAllIndustry() {
        return industryRepository.findAll().stream().map((x) -> new IndustryResponse(x)).toList();
    }

    @Transactional
    public void deleteIndustry(Integer id) {
        companyRepository.clearIndustry(id);
        industryRepository.deleteById(id);
    }
}
