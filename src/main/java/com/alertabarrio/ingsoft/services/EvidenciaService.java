package com.alertabarrio.ingsoft.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;

public interface EvidenciaService {
    EvidenciaResponseDTO save(EvidenciaSaveDTO dto);
    EvidenciaResponseDTO findById(Long id);
    EvidenciaResponseDTO update(Long id, EvidenciaSaveDTO dto);
    EvidenciaResponseDTO patch(Long id, EvidenciaSaveDTO dto); // ¡Aquí cambiamos Alerta por Evidencia!
    void delete(Long id);
    Page<EvidenciaResponseDTO> findAllPaginated(Pageable pageable);
}
