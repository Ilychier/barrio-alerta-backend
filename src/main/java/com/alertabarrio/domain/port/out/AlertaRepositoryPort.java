package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AlertaRepositoryPort {
    Alerta save(Alerta alerta);
    Optional<Alerta> findById(AlertaId id);
    boolean existsById(AlertaId id);
    void deleteById(AlertaId id);
    Page<Alerta> findAll(Pageable pageable);
    Page<Alerta> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
    Page<Alerta> findByUsuario_Barrio_IdAndFechaHoraBetween(Long barrioId, LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
}
