package com.userauth.application.service;

import com.userauth.application.dto.register.RegisterRequestDTO;
import com.userauth.domain.model.User;
import com.userauth.domain.ports.EmailServicePort;
import com.userauth.domain.ports.UserRepositoryPort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserApplicationService {

    private final UserRepositoryPort userRepository;
    private final EmailServicePort mailSender;

    public UserApplicationService(UserRepositoryPort userRepository, EmailServicePort mailSender) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }

    public void registerUser(RegisterRequestDTO dto) {
        // Validar si el email ya existe
        User existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }
        // Crear usuario
        User user = new User();

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setActive(false);
        user.setActivationToken(UUID.randomUUID().toString());
        user.setTokenExpiration(LocalDateTime.now().plusHours(2));
        // Guardar usuario
        userRepository.save(user);
        // Enviar email
        String activationLink = "http://localhost:8080/api/auth/activate/" + user.getActivationToken();
        mailSender.sendActivationEmail(user.getEmail(), activationLink);
    }
}

