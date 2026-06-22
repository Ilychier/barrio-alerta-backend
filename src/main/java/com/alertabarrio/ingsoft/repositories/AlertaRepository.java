package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}