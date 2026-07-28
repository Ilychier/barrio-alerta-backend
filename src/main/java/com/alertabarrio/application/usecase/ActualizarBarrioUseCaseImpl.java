package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ActualizarBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.ActualizarBarrioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class ActualizarBarrioUseCaseImpl implements ActualizarBarrioUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final CuadranteRepositoryPort cuadranteRepository;
    private final BarrioDomainMapper mapper;

    public ActualizarBarrioUseCaseImpl(BarrioRepositoryPort barrioRepository, CuadranteRepositoryPort cuadranteRepository, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.cuadranteRepository = cuadranteRepository;
        this.mapper = mapper;
    }

    @Override
    public BarrioDTO execute(ActualizarBarrioCommand command) {
        BarrioId id = new BarrioId(command.id());
        Barrio existente = barrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", command.id()));

        if (!existente.getNombre().equals(command.nombre()) && barrioRepository.existsByNombre(command.nombre())) {
            throw new ResourceConflictException("Barrio", command.nombre());
        }
        if (!cuadranteRepository.existsById(new CuadranteId(command.cuadranteId()))) {
            throw new ResourceNotFoundException("Cuadrante", command.cuadranteId());
        }

        Barrio actualizada = Barrio.reconstruir(command.id(), command.nombre(), command.cuadranteId());
        Barrio saved = barrioRepository.save(actualizada);
        return mapper.toDto(saved);
    }
}
