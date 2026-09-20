package com.fpt.kbase.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
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

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getSystemRole() { return systemRole; }
    public void setSystemRole(String systemRole) { this.systemRole = systemRole; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<Project> getCreatedProjects() { return createdProjects; }
    public void setCreatedProjects(List<Project> createdProjects) { this.createdProjects = createdProjects; }
    public List<Member> getMemberships() { return memberships; }
    public void setMemberships(List<Member> memberships) { this.memberships = memberships; }
    public List<Document> getUploadedDocuments() { return uploadedDocuments; }
    public void setUploadedDocuments(List<Document> uploadedDocuments) { this.uploadedDocuments = uploadedDocuments; }
}
