package com.userauth.domain.service;

import com.userauth.domain.model.ActivationToken;
import com.userauth.domain.model.User;
import com.userauth.domain.ports.ActivationTokenRepositoryPort;
import com.userauth.domain.ports.UserServicePort;
import com.userauth.exceptions.InvalidTokenException;
import com.userauth.exceptions.TokenAlreadyUsedException;
import com.userauth.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationService {

    private final ActivationTokenRepositoryPort tokenRepository;
    private final UserServicePort userService;
    private final PasswordEncoder passwordEncoder;

    public void validateToken(String token) throws TokenAlreadyUsedException, TokenExpiredException {
        // Validación básica del token
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("Token no puede estar vacío");
        }

        // Buscar token en la base de datos
        ActivationToken activationToken = tokenRepository.findByToken(token.trim());
        System.out.println("Token buscado: " + token); // Log para depuración

        if (activationToken == null) {
            throw new InvalidTokenException("Token no encontrado en la base de datos");
        }

        if (activationToken.isUsed()) {
            throw new TokenAlreadyUsedException("Este token ya fue utilizado");
        }

        if (activationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token expirado");
        }

        // Verificar que el usuario existe
        User user = userService.findById(activationToken.getUserId());
        if (user == null) {
            throw new InvalidTokenException("Usuario asociado al token no existe");
        }
    }
    public void activateUser(String token, String password) throws TokenExpiredException, TokenAlreadyUsedException {
        // 1. Validar el token primero
        validateToken(token);

        // 2. Obtener el token completo
        ActivationToken activationToken = tokenRepository.findByToken(token);
        if (activationToken == null) {
            throw new InvalidTokenException("Token no encontrado");
        }

        // 3. Obtener el usuario con mejor manejo de errores
        try {
            User user = userService.findById(activationToken.getUserId());

            // 4. Actualizar usuario
            user.setPassword(passwordEncoder.encode(password));
            user.setActive(true);
            userService.save(user);

            // 5. Marcar token como usado
            tokenRepository.markAsUsed(token);
        } catch (RuntimeException e) {
            throw new InvalidTokenException("Error activando usuario: " + e.getMessage());
        }
    }
}
