package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.port.in.CrearCuadranteUseCase;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class CrearCuadranteUseCaseImpl implements CrearCuadranteUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;
    private final CuadranteDomainMapper mapper;

    public CrearCuadranteUseCaseImpl(CuadranteRepositoryPort cuadranteRepository, CuadranteDomainMapper mapper) {
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public CuadranteDTO execute(CrearCuadranteCommand command) {
        Cuadrante cuadrante = Cuadrante.crear(command.nombreUnidad(), command.telefonoEmergencia(), command.emailEmergencia());
        Cuadrante saved = cuadranteRepository.save(cuadrante);
        return mapper.toDto(saved);
    }
}
