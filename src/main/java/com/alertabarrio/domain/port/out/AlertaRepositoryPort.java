package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.time.LocalDateTime;
import java.util.Optional;

public interface AlertaRepositoryPort {
    Alerta save(Alerta alerta);
    Optional<Alerta> findById(AlertaId id);
    boolean existsById(AlertaId id);
    void deleteById(AlertaId id);
    Pagina<Alerta> findAll(Paginacion paginacion);
    Pagina<Alerta> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin, Paginacion paginacion);
    Pagina<Alerta> findByUsuario_Barrio_IdAndFechaHoraBetween(Long barrioId, LocalDateTime inicio, LocalDateTime fin, Paginacion paginacion);
}
