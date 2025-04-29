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
        // Implementación en el paso de recuperación de contraseña
    }
}