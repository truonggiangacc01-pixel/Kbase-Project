package com.fpt.kbase.controller;

import com.fpt.kbase.dto.request.ForgotPasswordRequest;
import com.fpt.kbase.dto.request.LoginRequest;
import com.fpt.kbase.dto.request.ResetPasswordRequest;
import com.fpt.kbase.dto.response.JwtResponse;
import com.fpt.kbase.dto.response.MessageResponse;
import com.fpt.kbase.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Controller", description = "Các API dành cho việc Đăng nhập, Đăng xuất, Quên mật khẩu")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(summary = "Đăng nhập hệ thống", description = "Trả về JWT token để sử dụng cho các API khác.")
    @ApiResponse(responseCode = "200", description = "Đăng nhập thành công", 
                 content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(jwtResponse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Đăng xuất", description = "Đăng xuất tài khoản. Với JWT, phía Backend chỉ trả về OK. Phía Frontend cần xóa token ở localStorage.")
    @ApiResponse(responseCode = "200", description = "Đăng xuất thành công", 
                 content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return ResponseEntity.ok(new MessageResponse("Đăng xuất thành công!"));
    }

    @Operation(summary = "Quên mật khẩu (Gửi OTP)", description = "Yêu cầu hệ thống gửi OTP 6 số vào email của bạn qua Mailtrap.")
    @ApiResponse(responseCode = "200", description = "OTP đã được gửi", 
                 content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        try {
            authService.forgotPassword(request.getEmail());
            return ResponseEntity.ok(new MessageResponse("Mã OTP đã được gửi tới email của bạn qua Mailtrap!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @Operation(summary = "Đặt lại mật khẩu", description = "Sử dụng email, mã OTP và mật khẩu mới để đổi mật khẩu.")
    @ApiResponse(responseCode = "200", description = "Đặt lại mật khẩu thành công", 
                 content = @Content(schema = @Schema(implementation = MessageResponse.class)))
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());
            return ResponseEntity.ok(new MessageResponse("Đổi mật khẩu thành công! Bạn có thể đăng nhập lại."));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
