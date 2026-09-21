package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "domains")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Domain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "domain")
    private Set<Job> jobs = new HashSet<>();

    @OneToMany(mappedBy = "domain")
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "domain")
    private Set<UserInfo> userInfos = new HashSet<>();
}
