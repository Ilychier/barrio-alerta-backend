package com.alertabarrio.ingsoft.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alertabarrio.ingsoft.models.dtos.CategoriaResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaSaveDTO;

public interface CategoriaService {

    CategoriaResponseDTO save(CategoriaSaveDTO dto);

    CategoriaResponseDTO findById(Long id);

    CategoriaResponseDTO update(Long id, CategoriaSaveDTO dto);

    CategoriaResponseDTO patch(Long id, CategoriaSaveDTO dto);

    void delete(Long id);

    Page<CategoriaResponseDTO> findAllPaginated(Pageable pageable);  
     
}