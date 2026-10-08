package com.example.entity;

import com.example.enums.Status;
import com.example.enums.WorkingType;
import jakarta.annotation.Nullable;
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

    @Column(name = "title", length = 255, nullable = false, unique = true)
    private String title;

    @Column(name = "slug", length = 255, nullable = false, unique = true)
    private String slug;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "experience_max")
    private Integer experienceMax;

    @Column(name = "experience_min")
    private Integer experienceMin;

    @Enumerated(EnumType.STRING)
    @Column(name = "working_type", length = 50)
    private WorkingType workingType;

    @Column(name = "salary_max")
    private Integer salaryMax;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_negotiable", nullable = false)
    private Boolean salaryNegotiable = false;

    @Column(name = "city", length = 50)
    private String city;

    @Lob // Tương ứng với kiểu nvarchar(MAX) trong SQL Server
    @Column(name = "description", columnDefinition = "nvarchar(MAX)")
    private String description;

    @Lob
    @Column(name = "requirements", columnDefinition = "nvarchar(MAX)")
    private String requirements;

    @Lob
    @Column(name = "benefits", columnDefinition = "nvarchar(MAX)")
    private String benefits;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private Status status = Status.PENDING;

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

    @OneToMany(mappedBy = "job", cascade = CascadeType.REMOVE)
    Set<Application> applications = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "job_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    Set<Skill> skills = new HashSet<>();
}
