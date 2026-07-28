package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearAlertaCommand;
import com.alertabarrio.application.dto.AlertaDTO;
import com.alertabarrio.application.mapper.AlertaDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Alerta;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.in.ParchearAlertaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;

@UseCase
public class ParchearAlertaUseCaseImpl implements ParchearAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;
    private final AlertaDomainMapper mapper;

    public ParchearAlertaUseCaseImpl(AlertaRepositoryPort alertaRepository, AlertaDomainMapper mapper) {
        this.alertaRepository = alertaRepository;
        this.mapper = mapper;
    }

    @Override
    public AlertaDTO execute(ParchearAlertaCommand command) {
        AlertaId id = new AlertaId(command.id());
        Alerta existente = alertaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta", command.id()));

        String descripcion = command.descripcion() != null ? command.descripcion() : existente.getDescripcion();
        boolean esSos = command.esSos() != null ? command.esSos() : existente.isEsSos();
        Long usuarioId = command.usuarioId() != null ? command.usuarioId() : existente.getUsuarioId().value();
        Long categoriaId = command.categoriaId() != null ? command.categoriaId()
                : (existente.getCategoriaId() != null ? existente.getCategoriaId().value() : null);

        Alerta parcheada = Alerta.reconstruir(command.id(), descripcion, esSos,
                existente.getFechaHora(), usuarioId, categoriaId);
        Alerta saved = alertaRepository.save(parcheada);
        return mapper.toDto(saved);
    }
}
