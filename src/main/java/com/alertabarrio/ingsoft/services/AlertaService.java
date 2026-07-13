package com.alertabarrio.ingsoft.services;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alertabarrio.ingsoft.models.dtos.AlertaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.AlertaSaveDTO;

public interface AlertaService {
    AlertaResponseDTO save(AlertaSaveDTO dto);
    AlertaResponseDTO findById(Long id);
    AlertaResponseDTO update(Long id, AlertaSaveDTO dto);
    AlertaResponseDTO patch(Long id, AlertaSaveDTO dto);
    void delete(Long id);
    Page<AlertaResponseDTO> findAllPaginated(Pageable pageable);
    Page<AlertaResponseDTO> findByFecha(LocalDate fecha, Pageable pageable);
    Page<AlertaResponseDTO> findByBarrioAndFecha(Long barrioId, LocalDate fecha, Pageable pageable);
}
