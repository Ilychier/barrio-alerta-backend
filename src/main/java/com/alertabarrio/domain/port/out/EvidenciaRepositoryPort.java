package com.alertabarrio.domain.port.out;

import com.alertabarrio.domain.model.Evidencia;
import com.alertabarrio.domain.model.valueobject.EvidenciaId;
import com.alertabarrio.domain.model.valueobject.Pagina;
import com.alertabarrio.domain.model.valueobject.Paginacion;
import java.util.Optional;

public interface EvidenciaRepositoryPort {
    Evidencia save(Evidencia evidencia);
    Optional<Evidencia> findById(EvidenciaId id);
    boolean existsById(EvidenciaId id);
    void deleteById(EvidenciaId id);
    Pagina<Evidencia> findByAlertaId(Long alertaId, Paginacion paginacion);
    Pagina<Evidencia> findAll(Paginacion paginacion);
}
