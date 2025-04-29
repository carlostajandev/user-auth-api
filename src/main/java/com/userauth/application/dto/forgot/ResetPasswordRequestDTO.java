package com.userauth.application.dto.forgot;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "Nueva contraseña es requerida")
    private String newPassword;
}
