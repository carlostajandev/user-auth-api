package com.userauth.domain.ports;

import com.userauth.domain.model.ActivationToken;

public interface ActivationTokenRepositoryPort {
    ActivationToken save(ActivationToken token);
    ActivationToken findByToken(String token);
    void markAsUsed(String token);
}
