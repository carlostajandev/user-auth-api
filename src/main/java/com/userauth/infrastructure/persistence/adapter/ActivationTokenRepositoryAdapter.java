package com.userauth.infrastructure.persistence.adapter;
import com.userauth.domain.model.ActivationToken;
import com.userauth.domain.ports.ActivationTokenRepositoryPort;
import com.userauth.infrastructure.persistence.entity.ActivationTokenEntity;
import com.userauth.infrastructure.persistence.mapper.ActivationTokenMapper;
import com.userauth.infrastructure.persistence.repository.JpaActivationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivationTokenRepositoryAdapter implements ActivationTokenRepositoryPort {

    private final JpaActivationTokenRepository jpaRepository;
    private final ActivationTokenMapper mapper;

    @Override
    public ActivationToken save(ActivationToken token) {
        ActivationTokenEntity entity = mapper.toEntity(token);
        entity = jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public ActivationToken findByToken(String token) {
        // Modificar para mejor manejo de errores
        if (token == null || token.isBlank()) {
            return null;
        }

        return jpaRepository.findByToken(token.trim())
                .map(mapper::toDomain)
                .orElse(null);
    }

    @Override
    public void markAsUsed(String token) {
        jpaRepository.markAsUsed(token);
    }
}