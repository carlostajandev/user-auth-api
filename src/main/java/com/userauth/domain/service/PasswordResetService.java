package com.userauth.domain.service;

import com.userauth.domain.model.PasswordResetToken;
import com.userauth.domain.model.User;
import com.userauth.domain.ports.EmailServicePort;
import com.userauth.domain.ports.PasswordResetTokenRepositoryPort;
import com.userauth.domain.ports.UserServicePort;
import com.userauth.exceptions.InvalidTokenException;
import com.userauth.exceptions.TokenAlreadyUsedException;
import com.userauth.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenRepositoryPort tokenRepository;
    private final UserServicePort userService;
    private final EmailServicePort emailService;
    private final PasswordEncoder passwordEncoder;

    public void requestPasswordReset(String email) {
        User user = userService.findByEmail(email);
        if (user == null || !user.isActive()) {
            // No revelar que el usuario no existe por seguridad
            return;
        }

        // Eliminar tokens previos
        tokenRepository.deleteByUserId(user.getId());

        // Crear nuevo token
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(user.getId());
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusHours(2)); // Expira en 2 horas
        token.setUsed(false);

        tokenRepository.save(token);

        // Enviar email
        emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());
    }

    public void validateToken(String token) throws TokenExpiredException, TokenAlreadyUsedException {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null) {
            throw new InvalidTokenException("Token de recuperación inválido");
        }

        if (resetToken.isUsed()) {
            throw new TokenAlreadyUsedException("Este token ya ha sido utilizado");
        }

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("El token de recuperación ha expirado");
        }
    }

    public void resetPassword(String token, String newPassword) throws TokenExpiredException, TokenAlreadyUsedException {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);
        validateToken(token);

        User user = userService.findById(resetToken.getUserId());
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.save(user);

        tokenRepository.markAsUsed(token);
    }
}
