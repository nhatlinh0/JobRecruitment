package com.example.dto;

import com.example.entity.Domain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainResponse {
    private Integer id;
    private String name;
    private String slug;

    public DomainResponse (Domain domain) {
        if (domain != null) {
            this.id = domain.getId();
            this.name = domain.getName();
            this.slug = domain.getSlug();
        }
    }
}
