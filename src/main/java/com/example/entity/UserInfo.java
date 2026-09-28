package com.example.entity;

import com.example.enums.WorkingType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_max")
    private Integer salaryMax;

    @Column(name = "salary_negotiable", nullable = false)
    private boolean salaryNegotiable = false;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "experience_min")
    private Integer experienceMin;

    @Column(name = "experience_max")
    private Integer experienceMax;

    @Column(name = "image", length = 100)
    private String image;

    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "working_type")
    @Enumerated(EnumType.STRING)
    private WorkingType workingType;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToMany
    @JoinTable(
            name = "user_skills",
            joinColumns = @JoinColumn(name = "skill_id"),
            inverseJoinColumns = @JoinColumn(name = "user_info_id")
    )
    private Set<Skill> skills = new HashSet<>();
}
