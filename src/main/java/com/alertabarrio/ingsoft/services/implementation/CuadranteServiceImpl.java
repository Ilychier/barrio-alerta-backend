package com.alertabarrio.ingsoft.services.implementation; // <-- Paquete mantenido para la implementación

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.services.CuadranteService; // <-- Import de la interfaz corregido

@Service
public class CuadranteServiceImpl implements CuadranteService {

    private final CuadranteRepository cuadranteRepository;

    public CuadranteServiceImpl(CuadranteRepository cuadranteRepository) {
        this.cuadranteRepository = cuadranteRepository;
    }

    @Override
    public CuadranteResponseDTO save(CuadranteSaveDTO dto) {
        // Validación actualizada al teléfono
        if (cuadranteRepository.existsByTelefonoEmergencia(dto.telefonoEmergencia())) {
            throw new ResourceConflictException("A cuadrante with the phone '" + dto.telefonoEmergencia() + "' already exists.");
        }

        Cuadrante cuadrante = new Cuadrante();
        cuadrante.setNombreUnidad(dto.nombreUnidad());
        cuadrante.setTelefonoEmergencia(dto.telefonoEmergencia());

        return mapToDTO(cuadranteRepository.save(cuadrante));
    }

    @Override
    public CuadranteResponseDTO findById(Long id) {
        Cuadrante cuadrante = cuadranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", id));
        return mapToDTO(cuadrante);
    }

    @Override
    public CuadranteResponseDTO update(Long id, CuadranteSaveDTO dto) {
        Cuadrante cuadrante = cuadranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", id));

        // Validación actualizada al teléfono
        if (!cuadrante.getTelefonoEmergencia().equals(dto.telefonoEmergencia()) && cuadranteRepository.existsByTelefonoEmergencia(dto.telefonoEmergencia())) {
            throw new ResourceConflictException("A cuadrante with the phone '" + dto.telefonoEmergencia() + "' already exists.");
        }

        cuadrante.setNombreUnidad(dto.nombreUnidad());
        cuadrante.setTelefonoEmergencia(dto.telefonoEmergencia());

        return mapToDTO(cuadranteRepository.save(cuadrante));
    }

    @Override
    public CuadranteResponseDTO patch(Long id, CuadranteSaveDTO dto) {
        Cuadrante cuadrante = cuadranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", id));

        if (dto.nombreUnidad() != null) {
            cuadrante.setNombreUnidad(dto.nombreUnidad());
        }
        
        // Validación actualizada al teléfono
        if (dto.telefonoEmergencia() != null) {
            if (!cuadrante.getTelefonoEmergencia().equals(dto.telefonoEmergencia()) && cuadranteRepository.existsByTelefonoEmergencia(dto.telefonoEmergencia())) {
                throw new ResourceConflictException("A cuadrante with the phone '" + dto.telefonoEmergencia() + "' already exists.");
            }
            cuadrante.setTelefonoEmergencia(dto.telefonoEmergencia());
        }

        return mapToDTO(cuadranteRepository.save(cuadrante));
    }

    @Override
    public void delete(Long id) {
        if (!cuadranteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cuadrante", id);
        }
        cuadranteRepository.deleteById(id);
    }

    @Override
    public Page<CuadranteResponseDTO> findAllPaginated(Pageable pageable) {
        return cuadranteRepository.findAll(pageable).map(this::mapToDTO);
    }

    private CuadranteResponseDTO mapToDTO(Cuadrante entity) {
        return new CuadranteResponseDTO(
            entity.getId(),
            entity.getNombreUnidad(),
            entity.getTelefonoEmergencia()
        );
    }
}