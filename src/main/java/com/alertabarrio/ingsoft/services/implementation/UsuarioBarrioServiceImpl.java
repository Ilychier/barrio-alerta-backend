package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceConflictException;
import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UserResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UsuarioBarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UsuarioBarrioSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Barrio;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.models.entities.UsuarioBarrio;
import com.alertabarrio.ingsoft.repositories.BarrioRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.repositories.UsuarioBarrioRepository;
import com.alertabarrio.ingsoft.services.UsuarioBarrioService;

@Service
public class UsuarioBarrioServiceImpl implements UsuarioBarrioService {

    private final UsuarioBarrioRepository usuarioBarrioRepository;
    private final UserRepository userRepository;
    private final BarrioRepository barrioRepository;

    public UsuarioBarrioServiceImpl(
            UsuarioBarrioRepository usuarioBarrioRepository,
            UserRepository userRepository,
            BarrioRepository barrioRepository) {
        this.usuarioBarrioRepository = usuarioBarrioRepository;
        this.userRepository = userRepository;
        this.barrioRepository = barrioRepository;
    }

    @Override
    public UsuarioBarrioResponseDTO save(UsuarioBarrioSaveDTO dto) {
        if (usuarioBarrioRepository.existsByUsuarioIdAndBarrioId(dto.usuarioId(), dto.barrioId())) {
            throw new ResourceConflictException("The relation between user " + dto.usuarioId() + " and barrio " + dto.barrioId() + " already exists.");
        }

        User user = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));

        Barrio barrio = barrioRepository.findById(dto.barrioId())
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", dto.barrioId()));

        UsuarioBarrio entity = new UsuarioBarrio();
        entity.setUsuario(user);
        entity.setBarrio(barrio);

        return mapToDTO(usuarioBarrioRepository.save(entity));
    }

    @Override
    public UsuarioBarrioResponseDTO findById(Long id) {
        UsuarioBarrio entity = usuarioBarrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioBarrio", id));
        return mapToDTO(entity);
    }

    @Override
    public UsuarioBarrioResponseDTO update(Long id, UsuarioBarrioSaveDTO dto) {
        UsuarioBarrio entity = usuarioBarrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioBarrio", id));

        if (!(entity.getUsuario().getId().equals(dto.usuarioId()) && entity.getBarrio().getId().equals(dto.barrioId()))
                && usuarioBarrioRepository.existsByUsuarioIdAndBarrioId(dto.usuarioId(), dto.barrioId())) {
            throw new ResourceConflictException("The relation between user " + dto.usuarioId() + " and barrio " + dto.barrioId() + " already exists.");
        }

        User user = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));

        Barrio barrio = barrioRepository.findById(dto.barrioId())
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", dto.barrioId()));

        entity.setUsuario(user);
        entity.setBarrio(barrio);

        return mapToDTO(usuarioBarrioRepository.save(entity));
    }

    @Override
    public UsuarioBarrioResponseDTO patch(Long id, UsuarioBarrioSaveDTO dto) {
        UsuarioBarrio entity = usuarioBarrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UsuarioBarrio", id));

        Long targetUsuarioId = dto.usuarioId() != null ? dto.usuarioId() : entity.getUsuario().getId();
        Long targetBarrioId = dto.barrioId() != null ? dto.barrioId() : entity.getBarrio().getId();

        if (!(entity.getUsuario().getId().equals(targetUsuarioId) && entity.getBarrio().getId().equals(targetBarrioId))
                && usuarioBarrioRepository.existsByUsuarioIdAndBarrioId(targetUsuarioId, targetBarrioId)) {
            throw new ResourceConflictException("The relation between user " + targetUsuarioId + " and barrio " + targetBarrioId + " already exists.");
        }

        if (dto.usuarioId() != null) {
            User user = userRepository.findById(dto.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));
            entity.setUsuario(user);
        }

        if (dto.barrioId() != null) {
            Barrio barrio = barrioRepository.findById(dto.barrioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barrio", dto.barrioId()));
            entity.setBarrio(barrio);
        }

        return mapToDTO(usuarioBarrioRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!usuarioBarrioRepository.existsById(id)) {
            throw new ResourceNotFoundException("UsuarioBarrio", id);
        }
        usuarioBarrioRepository.deleteById(id);
    }

    @Override
    public Page<UsuarioBarrioResponseDTO> findAllPaginated(Pageable pageable) {
        return usuarioBarrioRepository.findAll(pageable).map(this::mapToDTO);
    }

    private UsuarioBarrioResponseDTO mapToDTO(UsuarioBarrio entity) {
        UserResponseDTO userDTO = null;
        if (entity.getUsuario() != null) {
            userDTO = new UserResponseDTO(
                entity.getUsuario().getId(),
                entity.getUsuario().getName(),
                entity.getUsuario().getEmail(),
                entity.getUsuario().getPhone(),
                entity.getUsuario().getAddress(),
                entity.getUsuario().getBarrio().getId()
            );
        }

        BarrioResponseDTO barrioDTO = null;
        if (entity.getBarrio() != null) {
            CuadranteResponseDTO cuadranteDTO = null;
            if (entity.getBarrio().getCuadrante() != null) {
                cuadranteDTO = new CuadranteResponseDTO(
                    entity.getBarrio().getCuadrante().getId(),
                    entity.getBarrio().getCuadrante().getNombreUnidad(),
                    entity.getBarrio().getCuadrante().getTelefonoEmergencia()
                );
            }
            barrioDTO = new BarrioResponseDTO(
                entity.getBarrio().getId(),
                entity.getBarrio().getNombre(),
                cuadranteDTO
            );
        }

        return new UsuarioBarrioResponseDTO(
            entity.getId(),
            userDTO,
            barrioDTO
        );
    }
}
