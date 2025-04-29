package com.userauth.application.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivationRequestDTO {
    @NotBlank(message = "La contraseña es requerida")
    private String password;
}
