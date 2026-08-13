package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.LocalidadEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalidadJpaRepository extends JpaRepository<LocalidadEntity, Long> {
    Page<LocalidadEntity> findByMunicipio_Id(Long municipioId, Pageable pageable);
}
