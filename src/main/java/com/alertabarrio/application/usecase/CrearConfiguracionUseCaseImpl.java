package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearConfiguracionCommand;
import com.alertabarrio.application.dto.ConfiguracionDTO;
import com.alertabarrio.application.mapper.ConfiguracionDomainMapper;
import com.alertabarrio.domain.model.Configuracion;
import com.alertabarrio.domain.port.in.CrearConfiguracionUseCase;
import com.alertabarrio.domain.port.out.ConfiguracionRepositoryPort;

@UseCase
public class CrearConfiguracionUseCaseImpl implements CrearConfiguracionUseCase {

    private final ConfiguracionRepositoryPort configuracionRepository;
    private final ConfiguracionDomainMapper mapper;

    public CrearConfiguracionUseCaseImpl(ConfiguracionRepositoryPort configuracionRepository, ConfiguracionDomainMapper mapper) {
        this.configuracionRepository = configuracionRepository;
        this.mapper = mapper;
    }

    @Override
    public ConfiguracionDTO execute(CrearConfiguracionCommand command) {
        Configuracion configuracion = Configuracion.crear(command.usuarioId(), command.recibirNotificaciones(), command.modoSilencioso());
        Configuracion saved = configuracionRepository.save(configuracion);
        return mapper.toDto(saved);
    }
}
