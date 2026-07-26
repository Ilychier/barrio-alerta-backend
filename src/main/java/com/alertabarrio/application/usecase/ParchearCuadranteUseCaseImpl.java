package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearCuadranteCommand;
import com.alertabarrio.application.dto.CuadranteDTO;
import com.alertabarrio.application.mapper.CuadranteDomainMapper;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Cuadrante;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.ParchearCuadranteUseCase;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class ParchearCuadranteUseCaseImpl implements ParchearCuadranteUseCase {

    private final CuadranteRepositoryPort cuadranteRepository;
    private final CuadranteDomainMapper mapper;

    public ParchearCuadranteUseCaseImpl(CuadranteRepositoryPort cuadranteRepository, CuadranteDomainMapper mapper) {
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public CuadranteDTO execute(ParchearCuadranteCommand command) {
        CuadranteId id = new CuadranteId(command.id());
        Cuadrante existente = cuadranteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuadrante", command.id()));

        String nombreUnidad = command.nombreUnidad() != null ? command.nombreUnidad() : existente.getNombreUnidad();
        String telefonoEmergencia = command.telefonoEmergencia() != null ? command.telefonoEmergencia() : existente.getTelefonoEmergencia();

        Cuadrante parcheada = Cuadrante.reconstruir(command.id(), nombreUnidad, telefonoEmergencia);
        Cuadrante saved = cuadranteRepository.save(parcheada);
        return mapper.toDto(saved);
    }
}
