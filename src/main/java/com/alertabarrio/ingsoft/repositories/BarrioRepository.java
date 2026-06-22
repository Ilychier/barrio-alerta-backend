package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.Barrio;

public interface BarrioRepository extends JpaRepository<Barrio, Long> {
    boolean existsByNombre(String nombre);
}