package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.model.valueobject.LocalidadId;
import com.alertabarrio.domain.port.in.CrearBarrioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;
import com.alertabarrio.domain.port.out.LocalidadRepositoryPort;

@UseCase
public class CrearBarrioUseCaseImpl implements CrearBarrioUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final CuadranteRepositoryPort cuadranteRepository;
    private final LocalidadRepositoryPort localidadRepository;
    private final BarrioDomainMapper mapper;

    public CrearBarrioUseCaseImpl(BarrioRepositoryPort barrioRepository, CuadranteRepositoryPort cuadranteRepository, LocalidadRepositoryPort localidadRepository, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.cuadranteRepository = cuadranteRepository;
        this.localidadRepository = localidadRepository;
        this.mapper = mapper;
    }

    @Override
    public BarrioDTO execute(CrearBarrioCommand command) {
        if (barrioRepository.existsByNombre(command.nombre())) {
            throw new ResourceConflictException("Barrio", command.nombre());
        }
        if (!cuadranteRepository.existsById(new CuadranteId(command.cuadranteId()))) {
            throw new ResourceNotFoundException("Cuadrante", command.cuadranteId());
        }
        if (!localidadRepository.findById(new LocalidadId(command.localidadId())).isPresent()) {
            throw new ResourceNotFoundException("Localidad", command.localidadId());
        }
        Barrio barrio = Barrio.crear(command.nombre(), command.cuadranteId(), command.localidadId());
        Barrio saved = barrioRepository.save(barrio);
        return mapper.toDto(saved);
    }
}
