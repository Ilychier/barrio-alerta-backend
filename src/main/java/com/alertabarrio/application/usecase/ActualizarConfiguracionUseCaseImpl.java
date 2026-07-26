package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarConfiguracionCommand;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.model.valueobject.ConfiguracionId;
import com.alertabarrio.domain.port.in.ActualizarConfiguracionUseCase;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;

@UseCase
public class ActualizarConfiguracionUseCaseImpl implements ActualizarConfiguracionUseCase {

    private final ConfiguracionRepositoryPort configuracionRepository;
    private final ConfiguracionDomainMapper mapper;

    public ActualizarConfiguracionUseCaseImpl(ConfiguracionRepositoryPort configuracionRepository, ConfiguracionDomainMapper mapper) {
        this.configuracionRepository = configuracionRepository;
        this.mapper = mapper;
    }

    @Override
    public ConfiguracionDTO execute(ActualizarConfiguracionCommand command) {
        ConfiguracionId id = new ConfiguracionId(command.id());
        configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuracion", command.id()));

        Configuracion actualizada = Configuracion.reconstruir(command.id(), command.usuarioId(), command.recibirNotificaciones(), command.modoSilencioso());
        Configuracion saved = configuracionRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
