package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.BarrioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BarrioJpaRepository extends JpaRepository<BarrioEntity, Long> {
    boolean existsByNombre(String nombre);
}
