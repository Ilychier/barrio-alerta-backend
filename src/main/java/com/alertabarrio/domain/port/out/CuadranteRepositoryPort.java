package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

import java.util.Optional;

public interface CuadranteRepositoryPort {

    Cuadrante save(Cuadrante cuadrante);

    Optional<Cuadrante> findById(CuadranteId id);

    boolean existsById(CuadranteId id);

    void deleteById(CuadranteId id);

    boolean existsByTelefonoEmergencia(String telefonoEmergencia);

    Pagina<Cuadrante> findAll(Paginacion paginacion);
}
