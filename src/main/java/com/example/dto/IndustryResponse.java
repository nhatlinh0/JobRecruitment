package com.example.dto;

import com.example.entity.Industry;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndustryResponse {
    private String name;
    private String description;

    public IndustryResponse (Industry industry) {
        if (industry != null) {
            this.name = industry.getName();
            this.description = industry.getDescription();
        }
    }
}
