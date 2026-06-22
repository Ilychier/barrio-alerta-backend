package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Evidencia;
import com.alertabarrio.ingsoft.repositories.EvidenciaRepository;
import com.alertabarrio.ingsoft.services.EvidenciaService;

@Service
public class EvidenciaServicelmpl implements EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;

    public EvidenciaServicelmpl(EvidenciaRepository evidenciaRepository) {
        this.evidenciaRepository = evidenciaRepository;
    }

    @Override
    public EvidenciaResponseDTO save(EvidenciaSaveDTO dto) {
        Evidencia evidencia = new Evidencia();
        evidencia.setArchivoUrl(dto.archivoUrl());
        evidencia.setFechaSubida(LocalDateTime.now());

        return mapToDTO(evidenciaRepository.save(evidencia), dto.alertaId());
    }

    @Override
    public EvidenciaResponseDTO findById(Long id) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", id));
        return mapToDTO(evidencia, null);
    }

    @Override
    public EvidenciaResponseDTO update(Long id, EvidenciaSaveDTO dto) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", id));

        evidencia.setArchivoUrl(dto.archivoUrl());

        return mapToDTO(evidenciaRepository.save(evidencia), dto.alertaId());
    }

    @Override
    public EvidenciaResponseDTO patch(Long id, EvidenciaSaveDTO dto) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", id));

        if (dto.archivoUrl() != null) evidencia.setArchivoUrl(dto.archivoUrl());

        return mapToDTO(evidenciaRepository.save(evidencia), dto.alertaId());
    }

    @Override
    public void delete(Long id) {
        if (!evidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evidencia", id);
        }
        evidenciaRepository.deleteById(id);
    }

    @Override
    public Page<EvidenciaResponseDTO> findAllPaginated(Pageable pageable) {
        return evidenciaRepository.findAll(pageable).map(entity -> mapToDTO(entity, null));
    }

    private EvidenciaResponseDTO mapToDTO(Evidencia entity, Long alertaId) {
        return new EvidenciaResponseDTO(
            entity.getId(),
            entity.getArchivoUrl(),
            entity.getFechaSubida(),
            alertaId
        );
    }
}
