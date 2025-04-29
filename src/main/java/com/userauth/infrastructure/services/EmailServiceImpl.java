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
        String activationLink = baseUrl + "/api/auth/activate/" + activationToken;

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
        message.setSubject("Restablecimiento de Contraseña");
        message.setText("Hemos recibido una solicitud para restablecer tu contraseña.\n\n" +
                "Para continuar con el proceso, haz clic en el siguiente enlace:\n" +
                resetLink + "\n\n" +
                "Este enlace expirará en 2 horas.\n\n" +
                "Si no solicitaste este cambio, puedes ignorar este mensaje.");

        try {
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Error al enviar email de reseteo: " + e.getMessage());
            throw new RuntimeException("Error al enviar email de reseteo", e);
        }
    }
}