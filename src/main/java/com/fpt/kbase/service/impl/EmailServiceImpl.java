package com.fpt.kbase.service.impl;

import com.fpt.kbase.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("KBase - Password Reset OTP");
        message.setText("Your OTP to reset password is: " + otp + "\nThis OTP will expire in 15 minutes.");

        mailSender.send(message);
    }
}
