package com.alertabarrio.domain.port.out.mascotas;

import com.alertabarrio.domain.model.mascotas.TipoMascota;
import com.alertabarrio.domain.model.mascotas.valueobject.TipoMascotaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia del catálogo {@link TipoMascota}.
 */
public interface TipoMascotaRepositoryPort {

    TipoMascota save(TipoMascota tipoMascota);

    Optional<TipoMascota> findById(TipoMascotaId id);

    boolean existsById(TipoMascotaId id);

    /**
     * Catálogo pequeño (2-5 registros). No necesita paginación:
     * devuelve lista directa para el dropdown del formulario.
     */
    List<TipoMascota> findAllActivos();

    Pagina<TipoMascota> findAll(Paginacion paginacion);
}
