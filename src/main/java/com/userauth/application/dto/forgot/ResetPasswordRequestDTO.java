package com.userauth.application.dto.forgot;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    @NotBlank(message = "New password is required")
    private String newPassword;
}
