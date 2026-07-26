package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.CategoriaDescripcion;
import com.alertabarrio.domain.model.valueobject.CategoriaDescripcionId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface CategoriaDescripcionRepositoryPort {
    CategoriaDescripcion save(CategoriaDescripcion cd);
    Optional<CategoriaDescripcion> findById(CategoriaDescripcionId id);
    boolean existsById(CategoriaDescripcionId id);
    void deleteById(CategoriaDescripcionId id);
    Page<CategoriaDescripcion> findByCategoriaId(Long categoriaId, Pageable pageable);
    Page<CategoriaDescripcion> findAll(Pageable pageable);
}
