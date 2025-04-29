package com.userauth.domain.ports;

public interface EmailServicePort {
    void sendActivationEmail(String email, String activationToken);
    void sendPasswordResetEmail(String email, String resetToken);
}

