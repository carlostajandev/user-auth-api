package com.userauth.application.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO
{
    @NotBlank(message = "Email es requerido")
    private String email;

    @NotBlank(message = "Contraseña es requerida")
    private String password;
}