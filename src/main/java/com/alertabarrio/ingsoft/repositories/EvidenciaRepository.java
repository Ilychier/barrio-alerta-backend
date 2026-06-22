package com.alertabarrio.ingsoft.repositories;

import com.alertabarrio.ingsoft.models.entities.Evidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvidenciaRepository extends JpaRepository<Evidencia, Long> {
}
