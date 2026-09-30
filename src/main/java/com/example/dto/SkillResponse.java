package com.example.dto;


import com.example.entity.Skill;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillResponse {
    private String name;
    private String domainName;

    public SkillResponse (Skill skill) {
        if (skill != null) {
            this.name = skill.getName();
            this.domainName = skill.getDomain().getName();
        }
    }
}
