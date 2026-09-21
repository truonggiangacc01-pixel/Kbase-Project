package com.fpt.kbase.service.impl;

import com.fpt.kbase.dto.request.LoginRequest;
import com.fpt.kbase.dto.response.JwtResponse;
import com.fpt.kbase.entity.PasswordReset;
import com.fpt.kbase.entity.User;
import com.fpt.kbase.repository.PasswordResetRepository;
import com.fpt.kbase.repository.UserRepository;
import com.fpt.kbase.security.JwtUtils;
import com.fpt.kbase.security.UserDetailsImpl;
import com.fpt.kbase.service.AuthService;
import com.fpt.kbase.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetRepository passwordResetRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EmailService emailService;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        // Kiểm tra kịch bản test: Sai email hoặc Sai password
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Xin hãy nhập lại email"));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Xin hãy nhập lại password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Lấy System Role đầu tiên
        String role = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .findFirst()
                .orElse("ROLE_USER");

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getFullName(),
                role);
    }

    @Override
    public void forgotPassword(String email) {
        // Kiểm tra email tồn tại không
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email không tồn tại trong hệ thống!");
        }

        // Xóa OTP cũ nếu có
        passwordResetRepository.deleteByEmail(email);

        // Sinh OTP 6 số
        String otp = String.format("%06d", new Random().nextInt(999999));

        PasswordReset passwordReset = new PasswordReset();
        passwordReset.setEmail(email);
        passwordReset.setOtp(otp);
        passwordReset.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        
        passwordResetRepository.save(passwordReset);

        // Gửi qua Mailtrap
        emailService.sendOtpEmail(email, otp);
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        // Kiểm tra kịch bản test: Sai email hoặc Sai OTP
        if (!userRepository.existsByEmail(email)) {
            throw new RuntimeException("Xin hãy nhập lại email");
        }

        PasswordReset passwordReset = passwordResetRepository.findByEmailAndOtp(email, otp)
                .orElseThrow(() -> new RuntimeException("Xin hãy nhập lại opt"));

        if (passwordReset.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP đã hết hạn!");
        }

        User user = userRepository.findByEmail(email).get();

        // Cập nhật mật khẩu mới
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        // Xóa OTP sau khi dùng
        passwordResetRepository.delete(passwordReset);
    }
}
