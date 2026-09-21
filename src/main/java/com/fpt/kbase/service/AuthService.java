package com.fpt.kbase.service;

import com.fpt.kbase.dto.request.LoginRequest;
import com.fpt.kbase.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    void forgotPassword(String email);
    void resetPassword(String email, String otp, String newPassword);
}
