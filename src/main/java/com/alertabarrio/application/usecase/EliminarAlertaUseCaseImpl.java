package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarAlertaCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.AlertaId;
import com.alertabarrio.domain.port.in.EliminarAlertaUseCase;
import com.alertabarrio.domain.port.out.AlertaRepositoryPort;

@UseCase
public class EliminarAlertaUseCaseImpl implements EliminarAlertaUseCase {

    private final AlertaRepositoryPort alertaRepository;

    public EliminarAlertaUseCaseImpl(AlertaRepositoryPort alertaRepository) {
        this.alertaRepository = alertaRepository;
    }

    @Override
    public void execute(EliminarAlertaCommand command) {
        AlertaId id = new AlertaId(command.id());
        if (!alertaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta", command.id());
        }
        alertaRepository.deleteById(id);
    }
}
