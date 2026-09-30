package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "domains")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @OneToMany(mappedBy = "domain")
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "domain")
    private Set<UserInfo> userInfos = new HashSet<>();

    @ManyToMany()
    @JoinTable(
            name = "domain_skill",
            joinColumns = @JoinColumn(name = "domain_id"),
            inverseJoinColumns = @JoinColumn(name ="skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

}
