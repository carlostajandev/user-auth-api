package com.userauth.domain.model;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private long id;
    private String fullName;
    private String email;
    private String password;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String activationToken;
    private LocalDateTime tokenExpiration;
}
