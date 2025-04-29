package com.userauth.domain.ports;

import com.userauth.domain.model.User;

import java.util.UUID;

public interface UserServicePort {
    User registerUser(String fullName, String email);
    User findByEmail(String email);
    void activateUser(String token, String password);
    User save(User user);
    User findById(long userId);
}
