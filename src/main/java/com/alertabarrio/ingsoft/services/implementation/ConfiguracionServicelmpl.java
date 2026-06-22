package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Configuracion;
import com.alertabarrio.ingsoft.repositories.ConfiguracionRepository;
import com.alertabarrio.ingsoft.services.ConfiguracionService;

@Service
public class ConfiguracionServicelmpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;

    public ConfiguracionServicelmpl(ConfiguracionRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    @Override
    public ConfiguracionResponseDTO save(ConfiguracionSaveDTO dto) {
        Configuracion config = new Configuracion();
        config.setRecibirNotificaciones(dto.recibirNotificaciones());
        config.setModoSilencioso(dto.modoSilencioso());

        return mapToDTO(configuracionRepository.save(config), dto.usuarioId());
    }

    @Override
    public ConfiguracionResponseDTO findById(Long id) {
        Configuracion config = configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", id));
        return mapToDTO(config, null);
    }

    @Override
    public ConfiguracionResponseDTO update(Long id, ConfiguracionSaveDTO dto) {
        Configuracion config = configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", id));

        config.setRecibirNotificaciones(dto.recibirNotificaciones());
        config.setModoSilencioso(dto.modoSilencioso());

        return mapToDTO(configuracionRepository.save(config), dto.usuarioId());
    }

    @Override
    public ConfiguracionResponseDTO patch(Long id, ConfiguracionSaveDTO dto) {
        Configuracion config = configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", id));

        if (dto.recibirNotificaciones() != null) config.setRecibirNotificaciones(dto.recibirNotificaciones());
        if (dto.modoSilencioso() != null) config.setModoSilencioso(dto.modoSilencioso());

        return mapToDTO(configuracionRepository.save(config), dto.usuarioId());
    }

    @Override
    public void delete(Long id) {
        if (!configuracionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Configuracion", id);
        }
        configuracionRepository.deleteById(id);
    }

    @Override
    public Page<ConfiguracionResponseDTO> findAllPaginated(Pageable pageable) {
        return configuracionRepository.findAll(pageable).map(entity -> mapToDTO(entity, null));
    }

    private ConfiguracionResponseDTO mapToDTO(Configuracion entity, Long usuarioId) {
        return new ConfiguracionResponseDTO(
            entity.getId(),
            usuarioId,
            entity.getRecibirNotificaciones(),
            entity.getModoSilencioso()
        );
    }
}
