package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;

public interface CuadranteRepository extends JpaRepository<Cuadrante, Long> {
    boolean existsByTelefonoEmergencia(String telefonoEmergencia);
}