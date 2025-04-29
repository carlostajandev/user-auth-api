package com.userauth.domain.ports;

import com.userauth.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    User findByEmail(String email);
    Optional<User> findById(Long userId);
}

