package com.alertabarrio.ingsoft.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.Configuracion;

public interface ConfiguracionRepository extends JpaRepository<Configuracion, Long> {
    Optional<Configuracion> findByUsuarioId(Long usuarioId);
}
