package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CuadranteRepositoryPort {

    Cuadrante save(Cuadrante cuadrante);

    Optional<Cuadrante> findById(CuadranteId id);

    boolean existsById(CuadranteId id);

    void deleteById(CuadranteId id);

    boolean existsByTelefonoEmergencia(String telefonoEmergencia);

    Page<Cuadrante> findAll(Pageable pageable);
}
