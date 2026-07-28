package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.util.Optional;

public interface CategoriaDescripcionRepositoryPort {
    CategoriaDescripcion save(CategoriaDescripcion cd);
    Optional<CategoriaDescripcion> findById(CategoriaDescripcionId id);
    boolean existsById(CategoriaDescripcionId id);
    void deleteById(CategoriaDescripcionId id);
    Pagina<CategoriaDescripcion> findByCategoriaId(Long categoriaId, Paginacion paginacion);
    Pagina<CategoriaDescripcion> findAll(Paginacion paginacion);
}
