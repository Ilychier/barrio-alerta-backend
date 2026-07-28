package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.port.in.CrearAlertaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;

import java.time.Clock;

@UseCase
public class CrearAlertaUseCaseImpl implements CrearAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;
    private final AlertaDomainMapper mapper;
    private final Clock clock;

    public CrearAlertaUseCaseImpl(AlertaRepositoryPort alertaRepository, AlertaDomainMapper mapper, Clock clock) {
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
        this.clock = clock;
    }

    @Override
    public AlertaDTO execute(CrearAlertaCommand command) {
        Alerta alerta = Alerta.crear(command.descripcion(), command.esSos(), command.usuarioId(), command.categoriaId(), clock);
        Alerta saved = alertaRepository.save(alerta);
        return mapper.toDto(saved);
    }
}
