package com.alertabarrio.application.usecase;

import com.alertabarrio.application.UseCase;
import com.alertabarrio.application.command.ParchearBarrioCommand;
import com.alertabarrio.application.dto.BarrioDTO;
import com.alertabarrio.application.mapper.BarrioDomainMapper;
import com.alertabarrio.domain.exception.ResourceConflictException;
import com.alertabarrio.domain.exception.ResourceNotFoundException;
import com.alertabarrio.domain.model.Barrio;
import com.alertabarrio.domain.model.valueobject.BarrioId;
import com.alertabarrio.domain.model.valueobject.CiudadId;
import com.alertabarrio.domain.model.valueobject.CuadranteId;
import com.alertabarrio.domain.port.in.ParchearBarrioUseCase;
import com.alertabarrio.domain.port.out.BarrioRepositoryPort;
import com.alertabarrio.domain.port.out.CiudadInfoPort;
import com.alertabarrio.domain.port.out.CuadranteRepositoryPort;

@UseCase
public class ParchearBarrioUseCaseImpl implements ParchearBarrioUseCase {

    private final BarrioRepositoryPort barrioRepository;
    private final CuadranteRepositoryPort cuadranteRepository;
    private final CiudadInfoPort ciudadInfoPort;
    private final BarrioDomainMapper mapper;

    public ParchearBarrioUseCaseImpl(BarrioRepositoryPort barrioRepository, CuadranteRepositoryPort cuadranteRepository, CiudadInfoPort ciudadInfoPort, BarrioDomainMapper mapper) {
        this.barrioRepository = barrioRepository;
        this.cuadranteRepository = cuadranteRepository;
        this.ciudadInfoPort = ciudadInfoPort;
        this.mapper = mapper;
    }

    @Override
    public BarrioDTO execute(ParchearBarrioCommand command) {
        BarrioId id = new BarrioId(command.id());
        Barrio existente = barrioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Barrio", command.id()));

        String nombre = command.nombre() != null ? command.nombre() : existente.getNombre();
        Long cuadranteId = command.cuadranteId() != null ? command.cuadranteId() : existente.getCuadranteId().value();
        Long ciudadId = command.ciudadId() != null ? command.ciudadId() : existente.getCiudadId().value();

        if (command.nombre() != null && !existente.getNombre().equals(nombre) && barrioRepository.existsByNombre(nombre)) {
            throw new ResourceConflictException("Barrio", nombre);
        }
        if (command.cuadranteId() != null && !cuadranteRepository.existsById(new CuadranteId(cuadranteId))) {
            throw new ResourceNotFoundException("Cuadrante", cuadranteId);
        }
        if (command.ciudadId() != null && !ciudadInfoPort.findById(new CiudadId(ciudadId)).isPresent()) {
            throw new ResourceNotFoundException("Ciudad", ciudadId);
        }

        Barrio parcheada = Barrio.reconstruir(command.id(), nombre, cuadranteId, ciudadId);
        Barrio saved = barrioRepository.save(parcheada);
        return mapper.toDto(saved);
    }
}
