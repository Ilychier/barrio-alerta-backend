package com.alertabarrio.ingsoft.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alertabarrio.ingsoft.models.entities.Evidencia;

public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {
    Page<Evidencia> findByAlertaId(Long alertaId, Pageable pageable);
}
