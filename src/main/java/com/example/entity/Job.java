package com.example.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "experience", length = 100)
    private String experience;

    @Column(name = "working_type", length = 50)
    private String workingType;

    @Column(name = "salary_range", length = 100)
    private String salaryRange;

    @Column(name = "position", length = 100)
    private String position;

    @Lob // Tương ứng với kiểu nvarchar(MAX) trong SQL Server
    @Column(name = "description", columnDefinition = "nvarchar(MAX)")
    private String description;

    @Lob
    @Column(name = "requirements", columnDefinition = "nvarchar(MAX)")
    private String requirements;

    @Lob
    @Column(name = "benefits", columnDefinition = "nvarchar(MAX)")
    private String benefits;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToMany(mappedBy = "jobs")
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "job")
    Set<Application> applications = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    Set<Skill> skills = new HashSet<>();
}
