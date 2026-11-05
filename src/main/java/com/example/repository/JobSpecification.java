package com.example.repository;

import com.example.dto.JobFilterRequest;
import com.example.entity.Job;
import com.example.enums.Status;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobSpecification {
    public static Specification<Job> filter(JobFilterRequest f) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> p = new ArrayList<>();

            p.add(cb.equal(root.get("status"), Status.ACCEPTED));

            if (f.getWorkingTypes() != null && !f.getWorkingTypes().isEmpty()) {
                p.add(root.get("workingType").in(f.getWorkingTypes()));
            }
            if (f.getDomainSlugs() != null && !f.getDomainSlugs().isEmpty()) {
                p.add(root.join("domain", JoinType.LEFT).get("slug").in(f.getDomainSlugs()));
            }
            if (f.getSkillSlugs() != null && !f.getSkillSlugs().isEmpty()) {
                p.add(root.join("skills", JoinType.LEFT).get("slug").in(f.getSkillSlugs()));
            }
            if (f.getCity() != null && !f.getCity().isBlank()) {
                p.add(cb.equal(root.get("city"), f.getCity()));
            }
            // khoảng lương của job giao với khoảng lương client chọn
            if (f.getSalaryMin() != null) {
                p.add(cb.greaterThanOrEqualTo(root.get("salaryMax"), f.getSalaryMin()));
            }
            if (f.getSalaryMax() != null) {
                p.add(cb.lessThanOrEqualTo(root.get("salaryMin"), f.getSalaryMax()));
            }
            // khoảng kinh nghiệm của job giao với khoảng client chọn
            if (f.getExperienceMin() != null) {
                p.add(cb.greaterThanOrEqualTo(root.get("experienceMax"), f.getExperienceMin()));
            }
            if (f.getExperienceMax() != null) {
                p.add(cb.lessThanOrEqualTo(root.get("experienceMin"), f.getExperienceMax()));
            }
            return cb.and(p.toArray(new Predicate[0]));
        };
    }
}
