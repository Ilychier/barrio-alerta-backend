package com.alertabarrio.infrastructure.persistence.repository;

import com.alertabarrio.infrastructure.persistence.entity.EvidenciaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvidenciaJpaRepository extends JpaRepository<EvidenciaEntity, Long> {
    Page<EvidenciaEntity> findByAlertaId(Long alertaId, Pageable pageable);
}
