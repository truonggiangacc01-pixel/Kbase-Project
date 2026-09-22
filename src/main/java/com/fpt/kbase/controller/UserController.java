package com.fpt.kbase.controller;

import com.fpt.kbase.dto.request.UserCreateRequest;
import com.fpt.kbase.dto.request.UserProfileUpdateRequest;
import com.fpt.kbase.dto.response.MessageResponse;
import com.fpt.kbase.dto.response.UserResponse;
import com.fpt.kbase.service.UserService;
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
@RequestMapping("/api/v1/users")
@Tag(name = "User Management", description = "Các API quản lý tài khoản và hồ sơ cá nhân")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserService userService;

    // ==========================================
    // CÁC API DÀNH RIÊNG CHO ADMIN
    // ==========================================

    @Operation(summary = "Lấy danh sách người dùng", description = "Lấy toàn bộ tài khoản trong hệ thống. (Chỉ ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<UserResponse> responses = userService.getAllUsers();
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Tạo mới người dùng", description = "Tạo tài khoản thủ công có thể chọn Role. (Chỉ ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateRequest request) {
        try {
            UserResponse response = userService.createUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Cập nhật người dùng", description = "Cập nhật tài khoản của một người khác. (Chỉ ADMIN)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserCreateRequest request) {
        try {
            UserResponse response = userService.updateUser(id, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // ==========================================
    // CÁC API HỒ SƠ CÁ NHÂN (PROFILE)
    // ==========================================

    @Operation(summary = "Lấy thông tin cá nhân", description = "Xem hồ sơ tài khoản đang đăng nhập.")
    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile() {
        try {
            UserResponse response = userService.getMyProfile();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Cập nhật thông tin cá nhân", description = "Đổi Họ Tên của tài khoản đang đăng nhập.")
    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        try {
            UserResponse response = userService.updateMyProfile(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
