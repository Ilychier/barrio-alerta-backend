package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.BarrioSaveDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.Cuadrante;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.CuadranteRepository;
import com.alertabarrio.ingsoft.services.BarrioService;

@Service
public class BarrioServiceImpl implements BarrioService {

    private final BarrioRepository barrioRepository;
    private final CuadranteRepository cuadranteRepository; // Inyectamos el repositorio mapping

    public BarrioServiceImpl(BarrioRepository barrioRepository, CuadranteRepository cuadranteRepository) {
        this.barrioRepository = barrioRepository;
        this.cuadranteRepository = cuadranteRepository;
    }

    @Override
    public BarrioResponseDTO save(BarrioSaveDTO dto) {
        if (barrioRepository.existsByNombre(dto.nombre())) {
            throw new ResourceConflictException("A barrio with the name '" + dto.nombre() + "' already exists.");
        }

        // Buscar y validar que el cuadrante foráneo exista
        Cuadrante cuadrante = cuadranteRepository.findById(dto.cuadranteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", dto.cuadranteId()));

        Barrio barrio = new Barrio();
        barrio.setNombre(dto.nombre());
        barrio.setCuadrante(cuadrante);

        return mapToDTO(barrioRepository.save(barrio));
    }

    @Override
    public BarrioResponseDTO findById(Long id) {
        Barrio barrio = barrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", id));
        return mapToDTO(barrio);
    }

    @Override
    public BarrioResponseDTO update(Long id, BarrioSaveDTO dto) {
        Barrio barrio = barrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", id));

        if (!barrio.getNombre().equals(dto.nombre()) && barrioRepository.existsByNombre(dto.nombre())) {
            throw new ResourceConflictException("A barrio with the name '" + dto.nombre() + "' already exists.");
        }

        // Buscar y validar el nuevo cuadrante
        Cuadrante cuadrante = cuadranteRepository.findById(dto.cuadranteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", dto.cuadranteId()));

        barrio.setNombre(dto.nombre());
        barrio.setCuadrante(cuadrante);

        return mapToDTO(barrioRepository.save(barrio));
    }

    @Override
    public BarrioResponseDTO patch(Long id, BarrioSaveDTO dto) {
        Barrio barrio = barrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", id));

        if (dto.nombre() != null) {
            if (!barrio.getNombre().equals(dto.nombre()) && barrioRepository.existsByNombre(dto.nombre())) {
                throw new ResourceConflictException("A barrio with the name '" + dto.nombre() + "' already exists.");
            }
            barrio.setNombre(dto.nombre());
        }

        if (dto.cuadranteId() != null) {
            Cuadrante cuadrante = cuadranteRepository.findById(dto.cuadranteId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", dto.cuadranteId()));
            barrio.setCuadrante(cuadrante);
        }

        return mapToDTO(barrioRepository.save(barrio));
    }

    @Override
    public void delete(Long id) {
        if (!barrioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Barrio", id);
        }
        barrioRepository.deleteById(id);
    }

    @Override
    public Page<BarrioResponseDTO> findAllPaginated(Pageable pageable) {
        return barrioRepository.findAll(pageable).map(this::mapToDTO);
    }

    private BarrioResponseDTO mapToDTO(Barrio entity) {
        // Mapeamos también la entidad interna Cuadrante usando su estructura DTO correspondiente
        CuadranteResponseDTO cuadranteDTO = null;
        if (entity.getCuadrante() != null) {
            cuadranteDTO = new CuadranteResponseDTO(
                entity.getCuadrante().getId(),
                entity.getCuadrante().getNombreUnidad(),
                entity.getCuadrante().getTelefonoEmergencia()
            );
        }

        return new BarrioResponseDTO(
            entity.getId(),
            entity.getNombre(),
            cuadranteDTO
        );
    }
}