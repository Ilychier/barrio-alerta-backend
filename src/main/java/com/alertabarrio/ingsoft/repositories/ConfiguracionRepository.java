package com.alertabarrio.ingsoft.repositories;

import com.alertabarrio.ingsoft.models.entities.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
}