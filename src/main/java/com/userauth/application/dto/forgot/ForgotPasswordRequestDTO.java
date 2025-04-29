package com.userauth.application.dto.forgot;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    @NotBlank(message = "Email es requerido")
    @Email(message = "Email debe ser válido")
    private String email;
}
