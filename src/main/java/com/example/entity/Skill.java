package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "skills")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @ManyToMany(mappedBy = "skills")
    private Set<Domain> domains;

    @ManyToMany(mappedBy = "skills")
    private Set<Job> jobs = new HashSet<>();

    @ManyToMany(mappedBy = "skills")
    private Set<UserInfo> userInfos = new HashSet<>();
}
