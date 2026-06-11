package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alertabarrio.ingsoft.models.dtos.CuadranteResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CuadranteSaveDTO;

public interface CuadranteService {

    CuadranteResponseDTO save(CuadranteSaveDTO dto);

    CuadranteResponseDTO findById(Long id);

    CuadranteResponseDTO update(Long id, CuadranteSaveDTO dto);

    CuadranteResponseDTO patch(Long id, CuadranteSaveDTO dto);

    void delete(Long id);

    Page<CuadranteResponseDTO> findAllPaginated(Pageable pageable);  
     
}