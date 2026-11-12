package com.example.dto;

import com.example.entity.Package;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PackageResponse {

    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer credits;
    private String description;

    public PackageResponse(Package packageEntity) {
        if (packageEntity != null) {
            this.id = packageEntity.getId();
            this.name = packageEntity.getName();
            this.price = packageEntity.getPrice();
            this.credits = packageEntity.getCredits();
            this.description = packageEntity.getDescription();
        }
    }
}