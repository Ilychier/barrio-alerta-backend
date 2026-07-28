package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.EliminarCuadranteCommand;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.EliminarCuadranteUseCase;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class EliminarCuadranteUseCaseImpl implements EliminarCuadranteUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;

    public EliminarCuadranteUseCaseImpl(CuadranteRepositoryPort cuadranteRepository) {
        this.cuadranteRepository = cuadranteRepository;
    }

    @Override
    public void execute(EliminarCuadranteCommand command) {
        CuadranteId id = new CuadranteId(command.id());
        if (!cuadranteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cuadrante", command.id());
        }
        cuadranteRepository.deleteById(id);
    }
}
