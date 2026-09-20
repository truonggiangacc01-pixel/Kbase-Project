package com.fpt.kbase.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_members") // Vẫn giữ tên bảng là project_members cho rõ ràng trong CSDL
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String projectRole; // "OWNER" or "MEMBER"

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    public Member() {}


}
