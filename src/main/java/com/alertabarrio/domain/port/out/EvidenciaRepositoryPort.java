package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface EvidenciaRepositoryPort {
    Evidencia save(Evidencia evidencia);
    Optional<Evidencia> findById(EvidenciaId id);
    boolean existsById(EvidenciaId id);
    void deleteById(EvidenciaId id);
    Page<Evidencia> findByAlertaId(Long alertaId, Pageable pageable);
    Page<Evidencia> findAll(Pageable pageable);
}
