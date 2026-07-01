package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.EvidenciaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.models.entities.Evidencia;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import com.alertabarrio.ingsoft.repositories.EvidenciaRepository;
import com.alertabarrio.ingsoft.services.EvidenciaService;

@Service
public class EvidenciaServicelmpl implements EvidenciaService {

    private final EvidenciaRepository evidenciaRepository;
    private final AlertaRepository alertaRepository;

    public EvidenciaServicelmpl(EvidenciaRepository evidenciaRepository, AlertaRepository alertaRepository) {
        this.evidenciaRepository = evidenciaRepository;
        this.alertaRepository = alertaRepository;
    }

    @Override
    public EvidenciaResponseDTO save(EvidenciaSaveDTO dto) {
        Alerta alerta = alertaRepository.findById(dto.alertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", dto.alertaId()));

        Evidencia evidencia = new Evidencia();
        evidencia.setArchivoUrl(dto.archivoUrl());
        evidencia.setFechaSubida(LocalDateTime.now());
        evidencia.setAlerta(alerta);

        return mapToDTO(evidenciaRepository.save(evidencia));
    }

    @Override
    public EvidenciaResponseDTO findById(Long id) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", id));
        return mapToDTO(evidencia);
    }

    @Override
    public EvidenciaResponseDTO update(Long id, EvidenciaSaveDTO dto) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", id));

        Alerta alerta = alertaRepository.findById(dto.alertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", dto.alertaId()));

        evidencia.setArchivoUrl(dto.archivoUrl());
        evidencia.setAlerta(alerta);

        return mapToDTO(evidenciaRepository.save(evidencia));
    }

    @Override
    public EvidenciaResponseDTO patch(Long id, EvidenciaSaveDTO dto) {
        Evidencia evidencia = evidenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evidencia", id));

        if (dto.archivoUrl() != null) evidencia.setArchivoUrl(dto.archivoUrl());

        if (dto.alertaId() != null) {
            Alerta alerta = alertaRepository.findById(dto.alertaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Alerta", dto.alertaId()));
            evidencia.setAlerta(alerta);
        }

        return mapToDTO(evidenciaRepository.save(evidencia));
    }

    @Override
    public void delete(Long id) {
        if (!evidenciaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Evidencia", id);
        }
        evidenciaRepository.deleteById(id);
    }

    @Override
    public Page<EvidenciaResponseDTO> findAllPaginated(Long alertaId, Pageable pageable) {
        if (alertaId != null) {
            return evidenciaRepository.findByAlertaId(alertaId, pageable).map(this::mapToDTO);
        }
        return evidenciaRepository.findAll(pageable).map(this::mapToDTO);
    }

    private EvidenciaResponseDTO mapToDTO(Evidencia entity) {
        return new EvidenciaResponseDTO(
            entity.getId(),
            entity.getArchivoUrl(),
            entity.getFechaSubida(),
            entity.getAlerta() != null ? entity.getAlerta().getId() : null
        );
    }
}
