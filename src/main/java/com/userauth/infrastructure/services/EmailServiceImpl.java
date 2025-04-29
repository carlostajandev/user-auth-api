package com.userauth.infrastructure.services;

import com.userauth.domain.ports.EmailServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailServicePort {

    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Override
    public void sendActivationEmail(String email, String activationToken) {
        String activationLink = baseUrl + "/api/auth/activate?token=" + activationToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Activate your account");
        message.setText("Please click the following link to activate your account and set your password:\n"
                + activationLink);

        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(String email, String resetToken) {
        String resetLink = baseUrl + "/api/auth/reset?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("We received a request to reset your password.\n\n" +
                "To proceed, please click the following link:\n" +
                resetLink + "\n\n" +
                "This link will expire in 2 hours.\n\n" +
                "If you did not request this change, you can safely ignore this message.");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
            throw new RuntimeException("Error sending password reset email", e);
        }
    }
}
