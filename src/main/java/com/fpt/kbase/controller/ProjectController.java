package com.fpt.kbase.controller;

import com.fpt.kbase.dto.request.ProjectRequest;
import com.fpt.kbase.dto.response.MessageResponse;
import com.fpt.kbase.dto.response.ProjectResponse;
import com.fpt.kbase.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project Management", description = "Các API quản lý dự án (Chỉ dành cho User đã đăng nhập)")
@SecurityRequirement(name = "bearerAuth") // Yêu cầu nhập token trên Swagger
@PreAuthorize("hasRole('USER')") // BẢO MẬT: CHỈ ROLE_USER MỚI ĐƯỢC PHÉP TRUY CẬP
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "Tạo dự án mới", description = "Tạo một dự án mới. User đang đăng nhập sẽ tự động trở thành Owner.")
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectRequest request) {
        try {
            ProjectResponse response = projectService.createProject(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Lấy danh sách dự án", description = "Lấy toàn bộ các dự án do user hiện tại làm chủ.")
    @GetMapping
    public ResponseEntity<?> getAllProjectsOfUser() {
        try {
            List<ProjectResponse> responses = projectService.getAllProjectsOfUser();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Lấy chi tiết dự án", description = "Lấy thông tin chi tiết của 1 dự án (Phải là chủ dự án mới xem được).")
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        try {
            ProjectResponse response = projectService.getProjectById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Cập nhật dự án", description = "Sửa tên và mô tả của dự án (Phải là chủ dự án).")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        try {
            ProjectResponse response = projectService.updateProject(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Xóa dự án", description = "Xóa vĩnh viễn dự án (Phải là chủ dự án).")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return ResponseEntity.ok(new MessageResponse("Xóa dự án thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
