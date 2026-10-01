package com.example.dto;

import com.example.entity.Domain;
import com.example.entity.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class DomainResponse {
    private Integer id;
    private String name;
    private String slug;
    private Set<String> skills;
    public DomainResponse (Domain domain) {
        if (domain != null) {
            this.id = domain.getId();
            this.name = domain.getName();
            this.slug = domain.getSlug();
            this.skills = domain.getSkills().stream().map(Skill::getName).collect(Collectors.toSet());

        }
    }
}
