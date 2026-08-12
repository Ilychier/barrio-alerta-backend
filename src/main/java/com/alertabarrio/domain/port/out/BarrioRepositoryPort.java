package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.util.Optional;

public interface BarrioRepositoryPort {
    Barrio save(Barrio barrio);
    Optional<Barrio> findById(BarrioId id);
    boolean existsById(BarrioId id);
    void deleteById(BarrioId id);
    boolean existsByNombre(String nombre);
    Pagina<Barrio> findAll(Paginacion paginacion);
    Pagina<Barrio> findAllByLocalidadId(Long localidadId, Paginacion paginacion);
}
