package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.CategoriaDescripcionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaDescripcionJpaRepository extends JpaRepository<CategoriaDescripcionEntity, Long> {
    Page<CategoriaDescripcionEntity> findByCategoriaId(Long categoriaId, Pageable pageable);
}
