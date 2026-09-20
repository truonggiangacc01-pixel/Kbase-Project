package com.fpt.kbase.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private boolean isActive = true;

    private String systemRole; // "ADMIN" or "USER"

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    // --- Bidirectional Relationships ---

    // A user can create many projects
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent infinite recursion
    private List<Project> createdProjects = new ArrayList<>();

    // A user can join many projects (as a member)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Member> memberships = new ArrayList<>();

    // A user can upload many documents
    @OneToMany(mappedBy = "uploadedBy", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Document> uploadedDocuments = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public User() {}


}
