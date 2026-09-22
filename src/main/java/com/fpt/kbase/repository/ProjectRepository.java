package com.fpt.kbase.repository;

import com.fpt.kbase.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    // Tìm toàn bộ các project do một user cụ thể tạo ra
    List<Project> findByCreatedById(Long userId);
    
}
