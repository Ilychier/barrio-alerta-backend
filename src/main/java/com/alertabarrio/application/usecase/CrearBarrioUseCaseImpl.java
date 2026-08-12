package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.CrearBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.CiudadId;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.CrearBarrioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.CiudadInfoPort;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class CrearBarrioUseCaseImpl implements CrearBarrioUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final CuadranteRepositoryPort cuadranteRepository;
    private final CiudadInfoPort ciudadInfoPort;
    private final BarrioDomainMapper mapper;

    public CrearBarrioUseCaseImpl(BarrioRepositoryPort barrioRepository, CuadranteRepositoryPort cuadranteRepository, CiudadInfoPort ciudadInfoPort, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.cuadranteRepository = cuadranteRepository;
        this.ciudadInfoPort = ciudadInfoPort;
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
        if (!ciudadInfoPort.findById(new CiudadId(command.ciudadId())).isPresent()) {
            throw new ResourceNotFoundException("Ciudad", command.ciudadId());
        }
        Barrio barrio = Barrio.crear(command.nombre(), command.cuadranteId(), command.ciudadId());
        Barrio saved = barrioRepository.save(barrio);
        return mapper.toDto(saved);
    }
}
