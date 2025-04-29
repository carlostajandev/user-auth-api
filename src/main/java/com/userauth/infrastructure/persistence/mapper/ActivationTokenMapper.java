package com.userauth.infrastructure.persistence.mapper;

import com.userauth.domain.model.ActivationToken;
import com.userauth.infrastructure.persistence.entity.ActivationTokenEntity;
import org.springframework.stereotype.Component;

@Component
public class ActivationTokenMapper {

    public ActivationToken toDomain(ActivationTokenEntity entity) {
        if (entity == null) {
            return null;
        }

        ActivationToken domain = new ActivationToken();
        domain.setId(entity.getId());
        domain.setToken(entity.getToken());
        domain.setUserId(entity.getUserId());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setExpiresAt(entity.getExpiresAt());
        domain.setUsed(entity.isUsed());

        return domain;
    }

    public ActivationTokenEntity toEntity(ActivationToken domain) {
        if (domain == null) {
            return null;
        }

        ActivationTokenEntity entity = new ActivationTokenEntity();
        entity.setId(domain.getId());
        entity.setToken(domain.getToken());
        entity.setUserId(domain.getUserId());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setExpiresAt(domain.getExpiresAt());
        entity.setUsed(domain.isUsed());

        return entity;
    }
}