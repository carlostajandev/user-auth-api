package com.userauth.domain.model;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PasswordResetToken {
    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;
}
