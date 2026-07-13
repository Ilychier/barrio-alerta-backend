package com.alertabarrio.ingsoft.services.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alertabarrio.ingsoft.exceptions.ResourceNotFoundException;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionResponseDTO;
import com.alertabarrio.ingsoft.models.dtos.ConfiguracionSaveDTO;
import com.alertabarrio.ingsoft.models.entities.Configuracion;
import com.alertabarrio.ingsoft.models.entities.User;
import com.alertabarrio.ingsoft.repositories.ConfiguracionRepository;
import com.alertabarrio.ingsoft.repositories.UserRepository;
import com.alertabarrio.ingsoft.services.ConfiguracionService;

@Service
public class ConfiguracionServicelmpl implements ConfiguracionService {

    private final ConfiguracionRepository configuracionRepository;
    private final UserRepository userRepository;

    public ConfiguracionServicelmpl(
            ConfiguracionRepository configuracionRepository,
            UserRepository userRepository) {
        this.configuracionRepository = configuracionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ConfiguracionResponseDTO save(ConfiguracionSaveDTO dto) {
        User user = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));

        Configuracion config = new Configuracion();
        config.setUsuario(user);
        config.setRecibirNotificaciones(dto.recibirNotificaciones());
        config.setModoSilencioso(dto.modoSilencioso());

        return mapToDTO(configuracionRepository.save(config));
    }

    private Configuracion getOrCreateConfig(Long id) {
        Configuracion config = configuracionRepository.findByUsuarioId(id).orElse(null);
        if (config == null) {
            config = configuracionRepository.findById(id).orElse(null);
        }
        if (config == null) {
            User user = userRepository.findById(id).orElse(null);
            if (user != null) {
                config = new Configuracion();
                config.setUsuario(user);
                config.setRecibirNotificaciones(true);
                config.setModoSilencioso(false);
                config = configuracionRepository.save(config);
            } else {
                throw new ResourceNotFoundException("Configuracion", id);
            }
        }
        return config;
    }

    @Override
    public ConfiguracionResponseDTO findById(Long id) {
        return mapToDTO(getOrCreateConfig(id));
    }

    @Override
    public ConfiguracionResponseDTO update(Long id, ConfiguracionSaveDTO dto) {
        Configuracion config = getOrCreateConfig(id);

        User user = userRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));

        config.setUsuario(user);
        config.setRecibirNotificaciones(dto.recibirNotificaciones());
        config.setModoSilencioso(dto.modoSilencioso());

        return mapToDTO(configuracionRepository.save(config));
    }

    @Override
    public ConfiguracionResponseDTO patch(Long id, ConfiguracionSaveDTO dto) {
        Configuracion config = getOrCreateConfig(id);

        if (dto.usuarioId() != null) {
            User user = userRepository.findById(dto.usuarioId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", dto.usuarioId()));
            config.setUsuario(user);
        }
        if (dto.recibirNotificaciones() != null) config.setRecibirNotificaciones(dto.recibirNotificaciones());
        if (dto.modoSilencioso() != null) config.setModoSilencioso(dto.modoSilencioso());

        return mapToDTO(configuracionRepository.save(config));
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
        return configuracionRepository.findAll(pageable).map(this::mapToDTO);
    }

    private ConfiguracionResponseDTO mapToDTO(Configuracion entity) {
        return new ConfiguracionResponseDTO(
            entity.getId(),
            entity.getUsuario() != null ? entity.getUsuario().getId() : null,
            entity.getRecibirNotificaciones(),
            entity.getModoSilencioso()
        );
    }
}
