package com.fpt.kbase.repository;

import com.fpt.kbase.entity.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByEmailAndOtp(String email, String otp);
    void deleteByExpiryDateBefore(LocalDateTime now);
    void deleteByEmail(String email);
}
