package com.example.entity;

import com.example.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "companies")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "address", length = 500, nullable = false)
    private String address;

    @Column(name = "company_size", length = 50, nullable = false)
    private String companySize;

    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Column(name = "logo_url", length = 500, nullable = false)
    private String logoUrl;

    @Column(name = "profile_pdf_url", length = 500, nullable = false)
    private String profilePdfUrl;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private Status status = Status.PENDING;

    @Column(name = "country", length = 50, nullable = false)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id")
    private Industry industry;

    @OneToMany(mappedBy = "company")
    private Set<User> user = new HashSet<>();

    @OneToMany(mappedBy = "company", cascade = CascadeType.REMOVE)
    private Set<Job> jobs = new HashSet<>();
}
