package com.alertabarrio.ingsoft.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alertabarrio.ingsoft.models.dtos.BarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.BarrioSaveDTO;

public interface BarrioService {

    BarrioResponseDTO save(BarrioSaveDTO dto);

    BarrioResponseDTO findById(Long id);

    BarrioResponseDTO update(Long id, BarrioSaveDTO dto);

    BarrioResponseDTO patch(Long id, BarrioSaveDTO dto);

    void delete(Long id);

    Page<BarrioResponseDTO> findAllPaginated(Pageable pageable);  
     
}