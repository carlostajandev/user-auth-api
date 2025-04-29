package com.userauth.infrastructure.persistence.mapper;

import com.userauth.domain.model.PasswordResetToken;
import com.userauth.infrastructure.persistence.entity.PasswordResetTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenMapper {

    public PasswordResetToken toDomain(PasswordResetTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setId(entity.getId());
        token.setToken(entity.getToken());
        token.setUserId(entity.getUserId());
        token.setCreatedAt(entity.getCreatedAt());
        token.setExpiresAt(entity.getExpiresAt());
        token.setUsed(entity.isUsed());

        return token;
    }

    public PasswordResetTokenEntity toEntity(PasswordResetToken domain) {
        if (domain == null) {
            return null;
        }

        PasswordResetTokenEntity entity = new PasswordResetTokenEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUserId(domain.getUserId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUsed(domain.isUsed());

        return entity;
    }
}
