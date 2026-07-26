package com.alertabarrio.infrastructure.persistence.adapter;

import com.alertabarrio.domain.model.User;
import com.alertabarrio.domain.model.valueobject.UsuarioId;
import com.alertabarrio.domain.port.out.UsuarioRepositoryPort;
import com.alertabarrio.infrastructure.persistence.entity.UserEntity;
import com.alertabarrio.infrastructure.persistence.mapper.UserEntityMapper;
import com.alertabarrio.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepositoryPort {

    private final UserJpaRepository jpaRepository;
    private final UserEntityMapper mapper;

    public UsuarioRepositoryAdapter(UserJpaRepository jpaRepository, UserEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserEntity entity = mapper.toEntity(user);
        if (user.getId() != null) {
            entity.setId(user.getId().value());
        }
        UserEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(UsuarioId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsById(UsuarioId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(UsuarioId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email)
                .map(mapper::toDomain);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable)
                .map(mapper::toDomain);
    }
}
