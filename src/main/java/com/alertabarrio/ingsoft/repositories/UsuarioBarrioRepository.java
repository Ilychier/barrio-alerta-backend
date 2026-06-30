package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.UsuarioBarrio;

public interface UsuarioBarrioRepository extends JpaRepository<UsuarioBarrio, Long> {
    boolean existsByUsuarioIdAndBarrioId(Long usuarioId, Long barrioId);
}
