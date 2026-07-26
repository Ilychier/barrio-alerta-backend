package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Categoria;
import com.alertabarrio.domain.model.valueobject.CategoriaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoriaRepositoryPort {

    Categoria save(Categoria categoria);

    Optional<Categoria> findById(CategoriaId id);

    boolean existsById(CategoriaId id);

    void deleteById(CategoriaId id);

    boolean existsByNombre(String nombre);

    Page<Categoria> findAll(Pageable pageable);
}
