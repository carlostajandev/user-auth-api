package com.userauth.application.dto.activation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActivationRequestDTO {
    @NotBlank(message = "Password is required")
    private String password;
}
