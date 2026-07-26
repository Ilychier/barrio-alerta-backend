package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BarrioRepositoryPort {
    Barrio save(Barrio barrio);
    Optional<Barrio> findById(BarrioId id);
    boolean existsById(BarrioId id);
    void deleteById(BarrioId id);
    boolean existsByNombre(String nombre);
    Page<Barrio> findAll(Pageable pageable);
}
