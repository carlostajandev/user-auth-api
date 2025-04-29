package com.userauth.domain.ports;


import com.userauth.domain.model.PasswordResetToken;

public interface PasswordResetTokenRepositoryPort {
    PasswordResetToken save(PasswordResetToken token);
    PasswordResetToken findByToken(String token);
    void markAsUsed(String token);
    void deleteByUserId(Long userId);
}