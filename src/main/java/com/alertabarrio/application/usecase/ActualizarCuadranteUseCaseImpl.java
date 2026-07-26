package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.ActualizarCuadranteUseCase;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class ActualizarCuadranteUseCaseImpl implements ActualizarCuadranteUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;
    private final CuadranteDomainMapper mapper;

    public ActualizarCuadranteUseCaseImpl(CuadranteRepositoryPort cuadranteRepository, CuadranteDomainMapper mapper) {
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public CuadranteDTO execute(ActualizarCuadranteCommand command) {
        CuadranteId id = new CuadranteId(command.id());
        cuadranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", command.id()));

        Cuadrante actualizada = Cuadrante.reconstruir(command.id(), command.nombreUnidad(), command.telefonoEmergencia());
        Cuadrante saved = cuadranteRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
