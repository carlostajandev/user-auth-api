package com.userauth.infrastructure.persistence.adapter;

import com.userauth.domain.model.User;
import com.userauth.domain.ports.UserRepositoryPort;
import com.userauth.infrastructure.persistence.entity.UserEntity;
import com.userauth.infrastructure.persistence.mapper.UserMapper;
import com.userauth.infrastructure.persistence.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        entity = jpaUserRepository.save(entity);
        return userMapper.toDomain(entity);
    }

    @Override
    public User findByEmail(String email) {
        Optional<UserEntity> entity = jpaUserRepository.findByEmail(email);
        return entity.map(userMapper::toDomain).orElse(null);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return jpaUserRepository.findById(userId)
                .map(userMapper::toDomain);
    }
}
