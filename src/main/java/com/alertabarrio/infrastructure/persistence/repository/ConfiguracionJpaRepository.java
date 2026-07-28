package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.ConfiguracionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfiguracionJpaRepository extends JpaRepository<ConfiguracionEntity, Long> {
    Optional<ConfiguracionEntity> findByUsuarioId(Long usuarioId);
}
