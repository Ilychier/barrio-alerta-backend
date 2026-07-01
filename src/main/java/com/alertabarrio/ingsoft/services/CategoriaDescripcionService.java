package com.alertabarrio.ingsoft.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.CategoriaDescripcionSaveDTO;

public interface CategoriaDescripcionService {
    CategoriaDescripcionResponseDTO save(CategoriaDescripcionSaveDTO dto);
    CategoriaDescripcionResponseDTO findById(Long id);
    CategoriaDescripcionResponseDTO update(Long id, CategoriaDescripcionSaveDTO dto);
    CategoriaDescripcionResponseDTO patch(Long id, CategoriaDescripcionSaveDTO dto);
    void delete(Long id);
    Page<CategoriaDescripcionResponseDTO> findAllPaginated(Long categoriaId, Pageable pageable);
}
