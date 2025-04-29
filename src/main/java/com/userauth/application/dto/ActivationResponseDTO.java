package com.userauth.application.dto;

import lombok.Data;

@Data
public class ActivationResponseDTO {
    private String message;

    public ActivationResponseDTO(String message) {
        this.message = message;
    }
}