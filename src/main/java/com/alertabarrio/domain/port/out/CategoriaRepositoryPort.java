package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;

import java.util.Optional;

public interface CategoriaRepositoryPort {

    Categoria save(Categoria categoria);

    Optional<Categoria> findById(CategoriaId id);

    boolean existsById(CategoriaId id);

    void deleteById(CategoriaId id);

    boolean existsByNombre(String nombre);

    Pagina<Categoria> findAll(Paginacion paginacion);
}
