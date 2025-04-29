package com.userauth.infrastructure.persistence.repository;

import com.userauth.infrastructure.persistence.entity.ActivationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

public interface JpaActivationTokenRepository extends JpaRepository<ActivationTokenEntity, Long> {
    Optional<ActivationTokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE ActivationTokenEntity t SET t.used = true WHERE t.token = ?1")
    void markAsUsed(String token);
}
