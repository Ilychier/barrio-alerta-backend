package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.in.ActualizarAlertaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;

@UseCase
public class ActualizarAlertaUseCaseImpl implements ActualizarAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;
    private final AlertaDomainMapper mapper;

    public ActualizarAlertaUseCaseImpl(AlertaRepositoryPort alertaRepository, AlertaDomainMapper mapper) {
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    @Override
    public AlertaDTO execute(ActualizarAlertaCommand command) {
        AlertaId id = new AlertaId(command.id());
        Alerta existente = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", command.id()));

        Alerta actualizada = Alerta.reconstruir(command.id(), command.descripcion(), command.esSos(),
                existente.getFechaHora(), command.usuarioId(), command.categoriaId());
        Alerta saved = alertaRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
