package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarBarrioCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.port.in.EliminarBarrioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;

@UseCase
public class EliminarBarrioUseCaseImpl implements EliminarBarrioUseCase {

    private final BarrioRepositoryPort barrioRepository;

    public EliminarBarrioUseCaseImpl(BarrioRepositoryPort barrioRepository) {
        this.barrioRepository = barrioRepository;
    }

    @Override
    public void execute(EliminarBarrioCommand command) {
        BarrioId id = new BarrioId(command.id());
        if (!barrioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Barrio", command.id());
        }
        barrioRepository.deleteById(id);
    }
}
