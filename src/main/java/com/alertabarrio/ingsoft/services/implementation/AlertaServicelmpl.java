package com.alertabarrio.ingsoft.services.implementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.entities.Alerta;
import com.alertabarrio.ingsoft.models.entities.Categoria;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.AlertaRepository;
import com.alertabarrio.ingsoft.repositories.CategoriaRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.AlertaService;

@Service
public class AlertaServicelmpl implements AlertaService {

    private final AlertaRepository alertaRepository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;

    public AlertaServicelmpl(
            AlertaRepository alertaRepository,
            UserRepository userRepository,
            CategoriaRepository categoriaRepository) {
        this.alertaRepository = alertaRepository;
        this.userRepository = userRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public AlertaResponseDTO save(AlertaSaveDTO dto) {
        User user = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.categoriaId()));

        Alerta alerta = new Alerta();
        alerta.setDescripcion(dto.descripcion());
        alerta.setEsSos(dto.esSos());
        alerta.setFechaHora(LocalDateTime.now());
        alerta.setUsuario(user);
        alerta.setCategoria(categoria);

        return mapToDTO(alertaRepository.save(alerta));
    }

    @Override
    public AlertaResponseDTO findById(Long id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));
        return mapToDTO(alerta);
    }

    @Override
    public AlertaResponseDTO update(Long id, AlertaSaveDTO dto) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));

        User user = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));

        Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.categoriaId()));

        alerta.setDescripcion(dto.descripcion());
        alerta.setEsSos(dto.esSos());
        alerta.setUsuario(user);
        alerta.setCategoria(categoria);

        return mapToDTO(alertaRepository.save(alerta));
    }

    @Override
    public AlertaResponseDTO patch(Long id, AlertaSaveDTO dto) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));

        if (dto.descripcion() != null) alerta.setDescripcion(dto.descripcion());
        if (dto.esSos() != null) alerta.setEsSos(dto.esSos());

        if (dto.usuarioId() != null) {
            User user = userRepository.findById(dto.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));
            alerta.setUsuario(user);
        }

        if (dto.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.categoriaId()));
            alerta.setCategoria(categoria);
        }

        return mapToDTO(alertaRepository.save(alerta));
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
        return alertaRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    public Page<AlertaResponseDTO> findByFecha(LocalDate fecha, Pageable pageable) {
        LocalDateTime inicioDia = fecha.atStartOfDay(); 
        LocalDateTime finDia = fecha.atTime(LocalTime.MAX); 
        return alertaRepository.findByFechaHoraBetween(inicioDia, finDia, pageable)
            .map(this::mapToDTO);
    }

    private AlertaResponseDTO mapToDTO(Alerta entity) {
        CategoriaResponseDTO categoriaDTO = null;
        if (entity.getCategoria() != null) {
            categoriaDTO = new CategoriaResponseDTO(
                entity.getCategoria().getId(),
                entity.getCategoria().getNombre(),
                entity.getCategoria().getIconoReferencia()
            );
        }

        return new AlertaResponseDTO(
            entity.getId(),
            entity.getDescripcion(),
            entity.getEsSos(),
            entity.getFechaHora(),
            entity.getUsuario() != null ? entity.getUsuario().getId() : null,
            categoriaDTO
        );
    }
}
