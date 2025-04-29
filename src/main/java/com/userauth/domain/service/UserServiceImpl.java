package com.userauth.domain.service;

import com.userauth.domain.model.ActivationToken;
import com.userauth.domain.model.User;
import com.userauth.domain.ports.ActivationTokenRepositoryPort;
import com.userauth.domain.ports.EmailServicePort;
import com.userauth.domain.ports.UserRepositoryPort;
import com.userauth.domain.ports.UserServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserServicePort {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final ActivationTokenRepositoryPort activationTokenRepositoryPort;
    private final EmailServicePort emailServicePort;

    @Override
    public User registerUser(String fullName, String email) {
        // 1. Create and save user
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setActive(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepositoryPort.save(user);

        // 2. Verify user has an ID
        if (savedUser.getId() == 0) {
            throw new RuntimeException("Error saving user - ID not generated");
        }

        // 3. Create token with correct ID
        ActivationToken token = new ActivationToken();
        token.setToken(UUID.randomUUID().toString());
        token.setUserId(savedUser.getId());
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiresAt(LocalDateTime.now().plusDays(1));
        token.setUsed(false);

        activationTokenRepositoryPort.save(token);

        // 4. Send email
        emailServicePort.sendActivationEmail(email, token.getToken());

        return savedUser;
    }

    @Override
    public User findByEmail(String email) {
        return userRepositoryPort.findByEmail(email);
    }

    @Override
    public void activateUser(String token, String password) {
        // Implementation in next step
    }

    @Override
    public User save(User user) {
        return userRepositoryPort.save(user);
    }

    @Override
    public User findById(long userId) {
        return userRepositoryPort.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}
