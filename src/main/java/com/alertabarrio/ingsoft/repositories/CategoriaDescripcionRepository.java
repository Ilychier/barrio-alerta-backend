package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.CategoriaDescripcion;

public interface CategoriaDescripcionRepository extends JpaRepository<CategoriaDescripcion, Long> {
    Page<CategoriaDescripcion> findByCategoriaId(Long categoriaId, Pageable pageable);
}
