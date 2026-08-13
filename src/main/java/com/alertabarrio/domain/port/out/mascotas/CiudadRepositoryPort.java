package com.alertabarrio.domain.port.out.mascotas;

import com.alertabarrio.domain.model.mascotas.Ciudad;
import com.alertabarrio.domain.model.mascotas.valueobject.CiudadId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

import java.util.Optional;

/**
 * Puerto de salida para persistencia del catálogo {@link Ciudad}.
 */
public interface CiudadRepositoryPort {

    Ciudad save(Ciudad ciudad);

    Optional<Ciudad> findById(CiudadId id);

    boolean existsById(CiudadId id);

    Pagina<Ciudad> findAll(Paginacion paginacion);
}
