package com.userauth.infrastructure.persistence.repository;

import com.userauth.infrastructure.persistence.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JpaPasswordResetTokenRepository extends JpaRepository<PasswordResetTokenEntity, Long> {
    Optional<PasswordResetTokenEntity> findByToken(String token);

    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetTokenEntity t SET t.used = true WHERE t.token = ?1")
    void markAsUsed(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetTokenEntity t WHERE t.userId = ?1")
    void deleteByUserId(Long userId);
}
