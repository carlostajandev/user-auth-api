package com.userauth.infrastructure.persistence.adapter;

import com.userauth.domain.model.PasswordResetToken;
import com.userauth.domain.ports.PasswordResetTokenRepositoryPort;
import com.userauth.infrastructure.persistence.entity.PasswordResetTokenEntity;
import com.userauth.infrastructure.persistence.mapper.PasswordResetTokenMapper;
import com.userauth.infrastructure.persistence.repository.JpaPasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepositoryPort {

    private final JpaPasswordResetTokenRepository jpaRepository;
    private final PasswordResetTokenMapper mapper;

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        PasswordResetTokenEntity entity = mapper.toEntity(token);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public PasswordResetToken findByToken(String token) {
        return jpaRepository.findByToken(token)
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void markAsUsed(String token) {
        jpaRepository.markAsUsed(token);
    }

    @Override
    public void deleteByUserId(Long userId) {
        jpaRepository.deleteByUserId(userId);
    }
}