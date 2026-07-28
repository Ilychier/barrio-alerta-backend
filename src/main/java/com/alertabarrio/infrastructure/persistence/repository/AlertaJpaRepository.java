package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.AlertaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AlertaJpaRepository extends JpaRepository<AlertaEntity, Long> {
    Page<AlertaEntity> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<AlertaEntity> findByUsuario_Barrio_IdAndFechaHoraBetween(Long barrioId, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
}
