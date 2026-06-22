package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import com.alertabarrio.ingsoft.services.AlertaService;

@Service
public class AlertaServicelmpl implements AlertaService {

    private final AlertaRepository alertaRepository;

    public AlertaServicelmpl(AlertaRepository alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Override
    public AlertaResponseDTO save(AlertaSaveDTO dto) {
        Alerta alerta = new Alerta();
        alerta.setTipo(dto.tipo());
        alerta.setDescripcion(dto.descripcion());
        alerta.setUbicacion(dto.ubicacion());
        alerta.setFechaHora(LocalDateTime.now());

        return mapToDTO(alertaRepository.save(alerta), dto.usuarioId());
    }

    @Override
    public AlertaResponseDTO findById(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));
        return mapToDTO(alerta, null);
    }

    @Override
    public AlertaResponseDTO update(Long id, AlertaSaveDTO dto) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));

        alerta.setTipo(dto.tipo());
        alerta.setDescripcion(dto.descripcion());
        alerta.setUbicacion(dto.ubicacion());

        return mapToDTO(alertaRepository.save(alerta), dto.usuarioId());
    }

    @Override
    public AlertaResponseDTO patch(Long id, AlertaSaveDTO dto) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));

        if (dto.tipo() != null) alerta.setTipo(dto.tipo());
        if (dto.descripcion() != null) alerta.setDescripcion(dto.descripcion());
        if (dto.ubicacion() != null) alerta.setUbicacion(dto.ubicacion());

        return mapToDTO(alertaRepository.save(alerta), dto.usuarioId());
    }

    @Override
    public void delete(Long id) {
        if (!alertaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta", id);
        }
        alertaRepository.deleteById(id);
    }

    @Override
    public Page<AlertaResponseDTO> findAllPaginated(Pageable pageable) {
        return alertaRepository.findAll(pageable).map(entity -> mapToDTO(entity, null));
    }

    private AlertaResponseDTO mapToDTO(Alerta entity, Long usuarioId) {
        return new AlertaResponseDTO(
            entity.getId(),
            entity.getTipo(),
            entity.getDescripcion(),
            entity.getUbicacion(),
            entity.getFechaHora(),
            usuarioId
        );
    }
}
