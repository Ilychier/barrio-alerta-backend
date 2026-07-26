package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.CuadranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuadranteJpaRepository extends JpaRepository<CuadranteEntity, Long> {
    boolean existsByTelefonoEmergencia(String telefonoEmergencia);
}
