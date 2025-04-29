package com.userauth.application.dto.login;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private String token;
    private String email;
    private String fullName;

    public LoginResponseDTO(String token, String email, String fullName) {
        this.token = token;
        this.email = email;
        this.fullName = fullName;
    }
}