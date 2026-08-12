package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Localidad;
import com.alertabarrio.domain.model.valueobject.LocalidadId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

import java.util.Optional;

public interface LocalidadRepositoryPort {
    Optional<Localidad> findById(LocalidadId id);
    Pagina<Localidad> findByMunicipioId(Long municipioId, Paginacion paginacion);
}
