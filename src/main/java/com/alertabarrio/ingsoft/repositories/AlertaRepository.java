package com.alertabarrio.ingsoft.repositories;

import com.alertabarrio.ingsoft.models.entities.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    // Aquí Spring Boot crea automáticamente los métodos: save(), findById(), delete(), etc.
}