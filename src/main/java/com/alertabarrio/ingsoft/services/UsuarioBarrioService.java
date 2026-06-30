package com.alertabarrio.ingsoft.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.alertabarrio.ingsoft.models.dtos.UsuarioBarrioResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.UsuarioBarrioSaveDTO;

public interface UsuarioBarrioService {
    UsuarioBarrioResponseDTO save(UsuarioBarrioSaveDTO dto);
    UsuarioBarrioResponseDTO findById(Long id);
    UsuarioBarrioResponseDTO update(Long id, UsuarioBarrioSaveDTO dto);
    UsuarioBarrioResponseDTO patch(Long id, UsuarioBarrioSaveDTO dto);
    void delete(Long id);
    Page<UsuarioBarrioResponseDTO> findAllPaginated(Pageable pageable);
}
