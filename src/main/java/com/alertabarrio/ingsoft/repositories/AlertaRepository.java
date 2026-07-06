package com.alertabarrio.ingsoft.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.alertabarrio.ingsoft.models.entities.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    Page<Alerta> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
}