package com.example.dto;


import com.example.entity.Skill;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillResponse {
    private Integer id;
    private String name;
    private String slug;

    public SkillResponse (Skill skill) {
        if (skill != null) {
            this.id = skill.getId();
            this.name = skill.getName();
            this.slug = skill.getSlug();
        }
    }
}
