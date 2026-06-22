package com.alertabarrio.ingsoft.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;

public interface ConfiguracionService {
    ConfiguracionResponseDTO save(ConfiguracionSaveDTO dto);
    ConfiguracionResponseDTO findById(Long id);
    ConfiguracionResponseDTO update(Long id, ConfiguracionSaveDTO dto);
    ConfiguracionResponseDTO patch(Long id, ConfiguracionSaveDTO dto);
    void delete(Long id);
    Page<ConfiguracionResponseDTO> findAllPaginated(Pageable pageable);
}
