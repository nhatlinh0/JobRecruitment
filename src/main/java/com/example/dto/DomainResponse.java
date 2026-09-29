package com.example.dto;

import com.example.entity.Domain;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomainResponse {
    private Integer id;
    private String name;

    public DomainResponse (Domain domain) {
        this.id = domain.getId();
        this.name = domain.getName();
    }
}
