package com.example.dto;

import com.example.entity.Industry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryResponse {
    private  Integer id;
    private String name;
    private String slug;
    private String description;

    public IndustryResponse (Industry industry) {
        if (industry != null) {
            this.id = industry.getId();
            this.name = industry.getName();
            this.slug = industry.getSlug();
            this.description = industry.getDescription();
        }
    }
}
