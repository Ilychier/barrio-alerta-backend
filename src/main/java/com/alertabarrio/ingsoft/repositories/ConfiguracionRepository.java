package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.Configuracion;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
}
