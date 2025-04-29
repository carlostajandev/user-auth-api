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
        // Basic token validation
        if (token == null || token.isBlank()) {
            throw new InvalidTokenException("Token cannot be empty");
        }

        // Search for token in the database
        ActivationToken activationToken = tokenRepository.findByToken(token.trim());
        System.out.println("Token searched: " + token); // Debug log

        if (activationToken == null) {
            throw new InvalidTokenException("Token not found in the database");
        }

        if (activationToken.isUsed()) {
            throw new TokenAlreadyUsedException("This token has already been used");
        }

        if (activationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException("Token has expired");
        }

        // Verify user exists
        User user = userService.findById(activationToken.getUserId());
        if (user == null) {
            throw new InvalidTokenException("User associated with the token does not exist");
        }
    }

    public void activateUser(String token, String password) throws TokenExpiredException, TokenAlreadyUsedException {
        // 1. Validate token first
        validateToken(token);

        // 2. Get the full token
        ActivationToken activationToken = tokenRepository.findByToken(token);
        if (activationToken == null) {
            throw new InvalidTokenException("Token not found");
        }

        // 3. Get user with better error handling
        try {
            User user = userService.findById(activationToken.getUserId());

            // 4. Update user
            user.setPassword(passwordEncoder.encode(password));
            user.setActive(true);
            userService.save(user);

            // 5. Mark token as used
            tokenRepository.markAsUsed(token);
        } catch (RuntimeException e) {
            throw new InvalidTokenException("Error activating user: " + e.getMessage());
        }
    }
}
